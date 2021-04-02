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
 * Elevator is responsible for controlling and therefore managing the state of a single elevator.
 */
public class Elevator extends Thread implements ElevatorApi {
    /**
     * An object that provides the scheduling functions to the Elevator.
     */
    protected final SchedulerApi scheduler;
    /**
     * The application configuration loader.
     */
    protected final Config config;
    /**
     * A object responsible for the simulation of the elevator's door.
     */
    protected final Door door;
    /**
     * The ArrivalSensor in charge of generating notifications for this Elevator.
     */
    protected final ArrivalSensor arrivalSensor;
    /**
     * An object responsible for the simulation of the elevator's motor.
     */
    protected final Motor motor;
    /**
     * A Map containing all of the ElevatorButtons by the floor they are associated with.
     */
    protected final Map<Integer, ElevatorButton> buttons;
    /**
     * A Map containing all of the ElevatorLamps by the floor they are associated with.
     */
    protected final Map<Integer, ElevatorLamp> lamps;
    /**
     * A Set of all the floor numbers which currently requested of this Elevator.
     */
    protected final Set<Integer> destinations;
    /**
     * A unique ID associated with the Elevator.
     */
    protected final int elevatorNumber;
    /**
     * The maximum floor number this elevator is responsible for servicing.
     */
    protected final int maxFloors;
    private final ScheduledExecutorService executor; // FIXME replace with simple threads
    /**
     * The Elevator's instance of Logger.
     */
    private final Logger logger;
    /**
     * The socket the Elevator is receiving messages on.
     */
    private final DatagramSocket socket;
    /**
     * The current floor number for the Elevator. This floor number is incremented at the deceleration point and therefore it is the last floor number before that point and the next floor number after that point.
     */
    protected int currentFloorNumber;
    /**
     * The current state for the Elevator.
     */
    private State state;
    /**
     * The floor number assigned to the Elevator, while it was idle. The is set to maxFloor plus 1 after it has been serviced.
     */
    private int idleDestination;

    /**
     * The default constructor for the Elevator.
     *
     * @param config         The application configuration loader.
     * @param scheduler      An object that provides the scheduling functions to the Elevator.
     * @param elevatorNumber A unique ID associated with the Elevator.
     * @param maxFloors      The maximum floor number this elevator is responsible for servicing.
     * @throws SocketException If the Elevator fails to bind to it's socket.
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
     * Get the current floor number for the Elevator. This floor number is incremented at the deceleration point and therefore it is the last floor number before that point and the next floor number after that point.
     *
     * @return The current floor number for the Elevator.
     */
    public synchronized int getCurrentFloorNumber() {
        return currentFloorNumber;
    }


    /**
     * Get if the Elevator's motor is set to the up direction. This method is only to be used for testing.
     *
     * @return If the Elevator's motor is set to the up direction.
     */
    public synchronized boolean getIsUp() {
        return motor.directionIsUp();
    }

    /**
     * Get if the Elevator's motor is running. This method is only to be used for testing.
     *
     * @return If the Elevator's motor is running.
     */
    public synchronized boolean getIsMoving() {
        return motor.isMoving();
    }

    /**
     * The run method for listening for messages on the socket.
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
                    3, input -> canAddDestination((Destination) input.get(0))));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * An override of the default override method, adding closing of the socket.
     */
    @Override
    public void interrupt() {
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int distanceTheFloor(Destination destination) {
        return state.distanceTheFloor(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addDestination(Destination destination) {
        state.addDestination(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean canAddDestination(Destination destination) {
        return state.canAddDestination(destination);
    }

    /**
     * Get if the Elevator should stop for the next floor, i.e., the next floor is one of it's destinations.
     *
     * @return If the Elevator should stop for the next floor.
     */
    public synchronized boolean stopForNextFloor() {
        return state.stopForNextFloor();
    }

    /**
     * Notify the Elevator that it is passing a floor.
     */
    public synchronized void passFloor() {
        state.setLamps();
        logger.info("Elevator " + elevatorNumber + " passing floor " + currentFloorNumber);

        if (elevatorNumber == config.getIntProperty("elevatorStuck")) state.scheduleCheckIfStuck();
    }

    /**
     * Notify the Elevator that it has arrived at a floor.
     */
    public synchronized void atFloor() {
        try {
            state.atFloor();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (elevatorNumber == config.getIntProperty("elevatorStuck")) state.scheduleCheckIfStuck();
    }

    /**
     * Check if the Elevator is stuck by seeing if it has made it too the next floor number.
     *
     * @param floor The next floor the Elevator should be on.
     * @param isUp  If the Elevator is going up.
     */
    private synchronized void checkIfStuck(int floor, boolean isUp) {
        if (isUp ? currentFloorNumber < floor : currentFloorNumber > floor) {
            logger.warning("Elevator" + elevatorNumber + " is stuck");
            state = new ElevatorStuck();
        }
    }

    /**
     * Get the current state of the Elevator. This method is only to be used for testing.
     *
     * @return The current state of the Elevator.
     */
    public synchronized ElevatorState getElevatorState() {
        return state.getElevatorState();
    }

    /**
     * Get the object simulating the elevator door. This method is only to be used for testing.
     *
     * @return The object simulating the elevator door.
     */
    public Door getDoor() {
        return door;
    }

    /**
     * The interface for a elevator state.
     */
    interface State {
        /**
         * Get the number of floors between the current and destination floors.
         *
         * @param destination The potential destination for the Elevator.
         * @return the distance between the two floors.
         */
        int distanceTheFloor(Destination destination);

        /**
         * Add the specified destination to the list of destinations.
         *
         * @param destination The new destination for the Elevator.
         */
        void addDestination(Destination destination);

        /**
         * Get if a destination is in the current path of the Elevator.
         * If idleDestination is set and the new destination is further away than the idleDestination, the Elevator will not accept the new destination because it can not guaranty that will not turn around at the idleDestination.
         *
         * @param destination The potential destination for the Elevator.
         * @return If the destination is in the current path of the Elevator.
         */
        boolean canAddDestination(Destination destination);

        /**
         * Turn off the previous lamp and turns on the next one.
         */
        void setLamps();

        /**
         * Get if the Elevator should stop for the next floor, i.e., the next floor is one of it's destinations.
         *
         * @return If the Elevator should stop for the next floor.
         */
        boolean stopForNextFloor();

        /**
         * Notify the state that it has arrived at a floor.
         */
        void atFloor() throws IOException, ClassNotFoundException;

        /**
         * Schedule the Elevator to check if its stuck after an amount of time that is greater than the amount of time it would take to travel between floors.
         */
        void scheduleCheckIfStuck();

        /**
         * Get the current state of the Elevator. This method is only to be used for testing.
         *
         * @return The current state of the Elevator.
         */
        ElevatorState getElevatorState();
    }

    /**
     * The state for a stationary elevator.
     */
    class ElevatorNotMoving implements State {
        /**
         * The default constructor for ElevatorNotMoving.
         */
        public ElevatorNotMoving() {
            logger.info("Elevator " + elevatorNumber + " State Changed to: Idle");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setLamps() {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int distanceTheFloor(Destination destination) {
            return abs(destination.getFloorNumber() - currentFloorNumber);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized void addDestination(Destination destination) {
            arrivalSensor.start();
            destinations.add(destination.getFloorNumber());
            state = destination.getFloorNumber() > currentFloorNumber ? new ElevatorMovingUp() : new ElevatorMovingDown();
            idleDestination = destination.getFloorNumber();
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public boolean stopForNextFloor() {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public void atFloor() {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         * An elevator that is not moving is always willing to accept a new destination.
         */
        @Override
        public boolean canAddDestination(Destination destination) {
            return true;
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public void scheduleCheckIfStuck() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.NotMoving;
        }

    }

    /**
     * The abstract state for an elevator which is moving.
     */
    abstract class MovingState implements State {
        /**
         * The default construction for MovingState.
         */
        public MovingState() {
            logger.info("Elevator " + elevatorNumber + " State Changed to: Moving");
        }

        /**
         * Get the lamp for the previous floor.
         *
         * @return The lamp for the previous floor.
         */
        abstract protected ElevatorLamp getPreviousLamp();

        /**
         * Get the floor numbers for the people waiting on the current floor, that are going in the direction the Elevator is currently moving.
         *
         * @return The floor numbers for the people waiting on the current floor, that are going in the direction the Elevator is currently moving.
         */
        abstract protected Floors getWaitingPeople() throws IOException, ClassNotFoundException;

        /**
         * Get the floor numbers for the people waiting on the current floor, that are going in the opposite direction of the Elevator. Set the state of the Elevator to be moving in the opposite direction.
         *
         * @return The floor numbers for the people waiting on the current floor, that are going in the opposite direction of the Elevator.
         */
        abstract protected Floors getWaitingPeopleTurnAround() throws IOException, ClassNotFoundException;

        /**
         * {@inheritDoc}
         */
        @Override
        public void addDestination(Destination destination) {
            if (arrivalSensor.isNotRunning()) {
                arrivalSensor.start();
            }
            destinations.add(destination.getFloorNumber());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setLamps() {
            ElevatorLamp previousLamp = getPreviousLamp();
            if (previousLamp != null && previousLamp.isLit()) {
                previousLamp.setLamp(false);
            }
            lamps.get(currentFloorNumber).setLamp(true);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void atFloor() throws IOException, ClassNotFoundException {
            buttons.get(currentFloorNumber).setOn(false);
            setLamps();
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
            if (floors.getFloors().isEmpty() && idleDestination == currentFloorNumber) {
                floors = getWaitingPeopleTurnAround();
                idleDestination = maxFloors + 1;
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
                        this.atFloor();
                    }
                    motor.setMoving(true);
                }
            } else {
                motor.setMoving(true);
            }
        }
    }

    /**
     * The state for the Elevator that is moving up.
     */
    class ElevatorMovingUp extends MovingState {
        /**
         * The default constructor for ElevatorMovingUp.
         */
        public ElevatorMovingUp() {
            motor.setDirectionIsUp(true);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int distanceTheFloor(Destination destination) {
            return abs(destination.getFloorNumber() - currentFloorNumber) +
                    (destination.isUp() ? maxFloors - currentFloorNumber : 0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean stopForNextFloor() {
            return destinations.contains(++currentFloorNumber);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean canAddDestination(Destination destination) {
            return destination.isUp() && destination.getFloorNumber() > currentFloorNumber &&
                    (idleDestination == maxFloors + 1 || destination.getFloorNumber() < idleDestination);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void scheduleCheckIfStuck() {
            executor.schedule(() -> checkIfStuck(currentFloorNumber + 1, true), config.getIntProperty("checkIfStuckDelay"), TimeUnit.SECONDS);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.MovingUp;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected ElevatorLamp getPreviousLamp() {
            return lamps.get(currentFloorNumber - 1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Floors getWaitingPeople() throws IOException, ClassNotFoundException {
            return scheduler.getWaitingPeopleUp(currentFloorNumber);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Floors getWaitingPeopleTurnAround() throws IOException, ClassNotFoundException {
            state = new ElevatorMovingDown();
            return scheduler.getWaitingPeopleDown(currentFloorNumber);
        }
    }

    /**
     * The state for the Elevator that is moving down.
     */
    private class ElevatorMovingDown extends MovingState {
        /**
         * The default constructor for ElevatorMovingDown.
         */
        public ElevatorMovingDown() {
            motor.setDirectionIsUp(false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int distanceTheFloor(Destination destination) {
            return abs(destination.getFloorNumber() - currentFloorNumber) + currentFloorNumber;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean canAddDestination(Destination destination) {
            return !destination.isUp() && destination.getFloorNumber() < currentFloorNumber &&
                    (idleDestination == maxFloors + 1 || destination.getFloorNumber() > idleDestination);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean stopForNextFloor() {
            return destinations.contains(--currentFloorNumber);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void scheduleCheckIfStuck() {
            executor.schedule(() -> checkIfStuck(currentFloorNumber - 1, false), config.getIntProperty("checkIfStuckDelay"), TimeUnit.SECONDS);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.MovingDown;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected ElevatorLamp getPreviousLamp() {
            return lamps.get(currentFloorNumber + 1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Floors getWaitingPeople() throws IOException, ClassNotFoundException {
            return scheduler.getWaitingPeopleDown(currentFloorNumber);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Floors getWaitingPeopleTurnAround() throws IOException, ClassNotFoundException {
            state = new ElevatorMovingUp();
            return scheduler.getWaitingPeopleUp(currentFloorNumber);
        }
    }

    /**
     * The state for a elevator that is stuck.
     */
    private class ElevatorStuck implements State {
        /**
         * The default constructor for ElevatorStuck.
         */
        public ElevatorStuck() {
            arrivalSensor.interrupt();
            motor.setMoving(false);
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public void setLamps() {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         * This method should never be called because a stuck elevator will never return for handleCanAddDestination.
         */
        @Override
        public int distanceTheFloor(Destination destination) {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public void addDestination(Destination destination) {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public boolean stopForNextFloor() {
            return false;
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public void atFloor() {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         * This method will always return false because a stuck elevator should never accept a new destination.
         */
        @Override
        public boolean canAddDestination(Destination destination) {
            return false;
        }

        /**
         * {@inheritDoc}
         * This method should never be called because it does not make sense for an elevator that is not moving.
         */
        @Override
        public void scheduleCheckIfStuck() {
            throw new RuntimeException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.Stuck;
        }
    }
}
