package ElevatorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import model.AckMessage;
import model.Destination;
import model.ElevatorState;
import model.Floors;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.Math.abs;

/**
 * The Elevator class represents a single elevator in the system
 *
 * @version Feb 27, 2021
 */
public class Elevator extends Thread implements ElevatorApi {

    protected final SchedulerApi scheduler;
    protected final Config config;
    protected final Door door;
    protected final ArrivalSensor arrivalSensor;
    protected final Motor motor;
    protected final Map<Integer, ElevatorButton> buttons;
    protected final Map<Integer, ElevatorLamp> lamps;
    protected final Set<Integer> destinations;
    protected final int elevatorNumber;
    protected final int maxFloors;
    private final ScheduledExecutorService executor;
    private final Logger logger;
    private final DatagramSocket socket;
    protected int currentFloorNumber;
    private State state;
    private boolean wasIdle;

    /**
     * Constructor for Elevator
     *
     * @param config
     * @param scheduler The system scheduler
     */
    public Elevator(Config config, SchedulerApi scheduler, int elevatorNumber, int maxFloors) throws SocketException {
        this.config = config;
        this.scheduler = scheduler;
        this.maxFloors = maxFloors;
        this.elevatorNumber = elevatorNumber;
        logger = Logger.getLogger(this.getClass().getName());
        door = new Door(config);
        arrivalSensor = new ArrivalSensor(config, this);
        motor = new Motor();
        buttons = new HashMap<>();
        lamps = new HashMap<>();
        currentFloorNumber = 0;
        state = new ElevatorNotMoving();

        for (int i = 0; i <= maxFloors; i++) {
            buttons.put(i, new ElevatorButton(i));
            lamps.put(i, new ElevatorLamp(elevatorNumber, i));
        }
        destinations = new HashSet<>();
        socket = new DatagramSocket(config.getIntProperty("elevatorPort") + elevatorNumber);
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Getter for the current floor number
     *
     * @return The current floor number
     */
    public synchronized int getCurrentFloorNumber() {
        return currentFloorNumber;
    }


    /**
     * @return true if the motor direction is upwards, false for downwards
     */
    public synchronized boolean getIsUp() {
        return motor.directionIsUp();
    }

    /**
     * @return true if the motor is moving
     */
    public synchronized boolean getIsMoving() {
        return motor.isMoving();
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        try {
            StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                    1, input -> distanceTheFloor((Destination) input.get(0)),
                    2, input -> {
                        addDestination((Destination) input.get(0));
                        return new AckMessage();
                    },
                    3, input -> canAddDestination((Destination) input.get(0)),
                    20, input -> {
                        interrupt();
                        return new AckMessage();
                    }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Interrupt method
     */
    @Override
    public void interrupt() {
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    /**
     * Gets the number of floors between the current and destination floors
     *
     * @param destination Potential destination for the elevator
     * @return the distance between the two floors
     */
    @Override
    public synchronized int distanceTheFloor(Destination destination) {
        return state.handleDistanceTheFloor(destination);
    }

    /**
     * Adds the specified floor number to the list of destinations
     *
     * @param destination The new destination for the Elevator
     */
    @Override
    public synchronized void addDestination(Destination destination) {
        state.handleAddDestination(destination);
    }

    @Override
    public synchronized boolean canAddDestination(Destination destination) {
        return state.handleCanAddDestination(destination);
    }

    /**
     * @return true if the elevator should stop at the next floor
     */
    public synchronized boolean stopForNextFloor() {
        return state.handleStopForNextFloor();
    }

    /**
     * Pass the floor without stopping
     */
    public synchronized void passFloor() {
        state.handleSetLamps();
        logger.info("Elevator " + elevatorNumber + " passing floor " + currentFloorNumber);

        if (this.elevatorNumber == config.getIntProperty("elevatorStuck")) state.scheduleCheckIfStuck();
    }

    /**
     * Actions for when the elevator stops at a floor
     */
    public synchronized void atFloor() {
        try {
            state.handleAtFloor();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (this.elevatorNumber == config.getIntProperty("elevatorStuck")) state.scheduleCheckIfStuck();
    }

    /**
     * Used to check if the elevator is stuck
     *
     * @param floor the next floor the elevator should be on
     * @param isUp  if it's going up
     */
    private synchronized void checkIfStuck(int floor, boolean isUp) {
        if (isUp ? currentFloorNumber < floor : currentFloorNumber > floor) {
            logger.warning("Elevator" + elevatorNumber + " is stuck");
            state = new ElevatorStuck();
        }
    }

    /**
     * Returns elevator state
     *
     * @return the state elevator
     */
    public synchronized ElevatorState getElevatorState() {
        return state.getElevatorState();
    }

    /**
     * Getter method for the door
     *
     * @return the door
     */
    public Door getDoor() {
        return door;
    }

    interface State {
        /**
         * Turns off the previous lamp and turns on the next one
         */
        void handleSetLamps();

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        int handleDistanceTheFloor(Destination destination);

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param destination The new destination for the Elevator
         */
        void handleAddDestination(Destination destination);

        /**
         * @return true if the elevator should stop at the next floor
         */
        boolean handleStopForNextFloor();

        /**
         * Actions for when the elevator stops at a floor
         */
        void handleAtFloor() throws IOException, ClassNotFoundException;

        /**
         * Check if elevator can add destination
         *
         * @param destination
         * @return if can be added
         */
        boolean handleCanAddDestination(Destination destination);

        /**
         * Checks to see if elevator is stuck
         */
        void scheduleCheckIfStuck();

        /**
         * Getter method for state
         *
         * @return the state of the elevator
         */
        ElevatorState getElevatorState();
    }

    /**
     * Represents a stationary elevator
     */
    class ElevatorNotMoving implements State {
        public ElevatorNotMoving() {
            logger.info("Elevator " + elevatorNumber + " State Changed to: Idle");
            wasIdle = true;
        }

        @Override
        public void handleSetLamps() {
            throw new RuntimeException();
        }

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        @Override
        public int handleDistanceTheFloor(Destination destination) {
            return abs(destination.getFloorNumber() - currentFloorNumber);
        }

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param destination The new destination for the Elevator
         */
        @Override
        public synchronized void handleAddDestination(Destination destination) {
            arrivalSensor.start();
            destinations.add(destination.getFloorNumber());
            state = destination.getFloorNumber() > currentFloorNumber ? new ElevatorMovingUp() : new ElevatorMovingDown();
        }

        /**
         * @return true if the elevator should stop at the next floor
         */
        @Override
        public boolean handleStopForNextFloor() {
            throw new RuntimeException();
        }

        /**
         * Actions for when the elevator stops at a floor
         */
        @Override
        public void handleAtFloor() {
            throw new RuntimeException();
        }

        /**
         * Check if elevator can add destination
         *
         * @param destination
         * @return if can be added
         */
        @Override
        public boolean handleCanAddDestination(Destination destination) {
            return true;
        }

        /**
         * Checks to see if elevator is stuck
         */
        @Override
        public void scheduleCheckIfStuck() {
        }

        /**
         * Get Elevator state
         *
         * @return the state
         */
        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.NotMoving;
        }

    }

    /**
     * Represents an elevator in motion
     */
    abstract class MovingState implements State {
        public MovingState() {
            logger.info("Elevator " + elevatorNumber + " State Changed to: Moving");
        }

        abstract protected ElevatorLamp getPreviousLamp();

        /**
         * @return the set of floors with people waiting for an elevator moving in the specified direction
         */
        abstract protected Floors getWaitingPeople() throws IOException, ClassNotFoundException;

        /**
         * Turn around before getting people
         *
         * @return the set of floors with people waiting for an elevator moving in the opposite direction
         */
        abstract protected Floors getWaitingPeopleTurnAround() throws IOException, ClassNotFoundException;

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param destination The new destination for the Elevator
         */
        @Override
        public void handleAddDestination(Destination destination) {
            if (arrivalSensor.isNotRunning()) {
                arrivalSensor.start();
            }
            destinations.add(destination.getFloorNumber());
        }

        /**
         * Turns off the previous lamp and turns on the next one
         */
        @Override
        public void handleSetLamps() {
            ElevatorLamp previousLamp = getPreviousLamp();
            if (previousLamp != null && previousLamp.isLit()) {
                previousLamp.setLamp(false);
            }
            lamps.get(currentFloorNumber).setLamp(true);
        }

        /**
         * Actions for when the elevator stops at a floor
         */
        @Override
        public void handleAtFloor() throws IOException, ClassNotFoundException {
            buttons.get(currentFloorNumber).setOn(false);
            handleSetLamps();
            logger.info("Elevator " + elevatorNumber + " stopped at floor " + currentFloorNumber);
            motor.setMoving(false);

            while (!door.isOpen()) {
                door.open();
                if (!door.isOpen())
                    logger.warning("Elevator " + elevatorNumber + " doors stuck closed at floor " + currentFloorNumber);
            }

            try {
                Thread.sleep(config.getIntProperty("waitTime"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            destinations.remove(currentFloorNumber);
            Floors floors = getWaitingPeople();
            if (floors.getFloors().isEmpty() && wasIdle) {
                floors = getWaitingPeopleTurnAround();
                wasIdle = false;
            }
            floors.getFloors().forEach(destination -> buttons.get(destination).setOn(true));
            destinations.addAll(floors.getFloors());

            while (door.isOpen()) {

                door.close();
                if (door.isOpen())
                    logger.warning("Elevator " + elevatorNumber + " doors stuck open at floor " + currentFloorNumber);
            }

            if (destinations.isEmpty()) {
                destinations.addAll(scheduler.getWaitingPeople(currentFloorNumber).getFloors());
                if (destinations.isEmpty()) {
                    arrivalSensor.interrupt();
                    state = new ElevatorNotMoving();
                } else {
                    if (destinations.stream().anyMatch(floor -> floor < currentFloorNumber)) {
                        state = new ElevatorMovingDown();
                    } else {
                        state = new ElevatorMovingUp();
                    }
                    //Handle the edge case when the elevator is turning around on the floor where it needs to pick up someone.
                    if (destinations.contains(currentFloorNumber)) {
                        atFloor();
                    }
                    motor.setMoving(true);
                }
            } else {
                motor.setMoving(true);
            }
        }
    }

    /**
     * Represents an elevator moving upwards
     */
    class ElevatorMovingUp extends MovingState {

        public ElevatorMovingUp() {
            motor.setDirectionIsUp(true);
        }

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        @Override
        public int handleDistanceTheFloor(Destination destination) {
            return abs(destination.getFloorNumber() - currentFloorNumber) +
                    (destination.isUp() ? maxFloors - currentFloorNumber : 0);
        }

        /**
         * @return true if the elevator should stop at the next floor
         */
        @Override
        public boolean handleStopForNextFloor() {
            return destinations.contains(++currentFloorNumber);
        }

        /**
         * Check if elevator can add destination
         *
         * @param destination
         * @return if can be added
         */
        @Override
        public boolean handleCanAddDestination(Destination destination) {
            return destination.isUp() && destination.getFloorNumber() > currentFloorNumber;
        }

        /**
         * Checks to see if elevator is stuck
         */
        @Override
        public void scheduleCheckIfStuck() {
            executor.schedule(() -> checkIfStuck(currentFloorNumber + 1, true), config.getIntProperty("checkIfStuckDelay"), TimeUnit.SECONDS);
        }

        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.MovingUp;
        }

        /**
         * @return the previously lit elevator lamp
         */
        @Override
        protected ElevatorLamp getPreviousLamp() {
            return lamps.get(currentFloorNumber - 1);
        }

        /**
         * @return the set of floors with people waiting for an elevator moving upwards
         */
        @Override
        protected Floors getWaitingPeople() throws IOException, ClassNotFoundException {
            return scheduler.getWaitingPeopleUp(currentFloorNumber);
        }

        /**
         * @return the set of floors with people waiting for an elevator moving downwards
         */
        @Override
        protected Floors getWaitingPeopleTurnAround() throws IOException, ClassNotFoundException {
            state = new ElevatorMovingDown();
            return scheduler.getWaitingPeopleDown(currentFloorNumber);
        }
    }

    /**
     * Represents an elevator moving downwards
     */
    private class ElevatorMovingDown extends MovingState {
        public ElevatorMovingDown() {
            motor.setDirectionIsUp(false);
        }

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        @Override
        public int handleDistanceTheFloor(Destination destination) {
            return abs(destination.getFloorNumber() - currentFloorNumber) + currentFloorNumber;
        }

        /**
         * Returns if destination can be added
         *
         * @param destination
         * @return if it can be added
         */
        @Override
        public boolean handleCanAddDestination(Destination destination) {
            return !destination.isUp() && destination.getFloorNumber() < currentFloorNumber;
        }

        /**
         * @return true if the elevator should stop at the next floor
         */
        @Override
        public boolean handleStopForNextFloor() {
            return destinations.contains(--currentFloorNumber);
        }

        /**
         * Checks to see if elevator is stuck
         */
        @Override
        public void scheduleCheckIfStuck() {
            executor.schedule(() -> checkIfStuck(currentFloorNumber - 1, false), config.getIntProperty("checkIfStuckDelay"), TimeUnit.SECONDS);
        }

        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.MovingDown;
        }

        /**
         * @return the previously lit elevator lamp
         */
        @Override
        protected ElevatorLamp getPreviousLamp() {
            return lamps.get(currentFloorNumber + 1);
        }

        /**
         * @return the set of floors with people waiting for an elevator moving downwards
         */
        @Override
        protected Floors getWaitingPeople() throws IOException, ClassNotFoundException {
            return scheduler.getWaitingPeopleDown(currentFloorNumber);
        }

        /**
         * @return the set of floors with people waiting for an elevator moving upwards
         */
        @Override
        protected Floors getWaitingPeopleTurnAround() throws IOException, ClassNotFoundException {
            state = new ElevatorMovingUp();
            return scheduler.getWaitingPeopleUp(currentFloorNumber);
        }
    }

    private class ElevatorStuck implements State {
        public ElevatorStuck() {
            arrivalSensor.interrupt();
            motor.setMoving(false);
        }

        @Override
        public void handleSetLamps() {
            throw new RuntimeException();
        }

        @Override
        public int handleDistanceTheFloor(Destination destination) {
            throw new RuntimeException();
        }

        @Override
        public void handleAddDestination(Destination destination) {
            throw new RuntimeException();
        }

        @Override
        public boolean handleStopForNextFloor() {
            return false;
        }

        @Override
        public void handleAtFloor() {
            throw new RuntimeException();
        }

        @Override
        public boolean handleCanAddDestination(Destination destination) {
            return false;
        }

        @Override
        public void scheduleCheckIfStuck() {
            throw new RuntimeException();
        }

        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.Stuck;
        }
    }
}
