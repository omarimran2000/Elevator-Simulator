package ElevatorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.lang.Math.abs;

/**
 * The Elevator class represents a single elevator in the system
 *
 * @version Feb 27, 2021
 */
public class Elevator implements Runnable, ElevatorApi {

    protected final SchedulerApi scheduler;
    protected final Config config;
    protected final Door door;
    protected final ArrivalSensor arrivalSensor;
    protected final Motor motor;
    protected final Map<Integer, ElevatorButton> buttons;
    protected final Map<Integer, ElevatorLamp> lamps;
    protected final Set<Integer> destinationsInPath;
    protected final Set<Integer> destinationsOutOfPath;
    protected final int elevatorNumber;
    protected final int maxFloors;
    private final Logger logger;
    protected int currentFloorNumber;
    private State state;
    private DatagramSocket socket;

    /**
     * Constructor for Elevator
     *
     * @param config
     * @param scheduler The system scheduler
     */
    public Elevator(Config config, SchedulerApi scheduler, int elevatorNumber, int maxFloors) {
        this.config = config;
        this.scheduler = scheduler;
        this.maxFloors = maxFloors;
        this.elevatorNumber = elevatorNumber;
        logger = Logger.getLogger(this.getClass().getName());
        door = new Door();
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
        destinationsInPath = new HashSet<>();
        destinationsOutOfPath = new HashSet<>();
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
                    1, input -> distanceTheFloor((int)input.get(0), (boolean) input.get(1)),
                    2, input -> {
                        addDestination((int) input.get(0), (boolean) input.get(1));
                        return new AckMessage();
                    }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the number of floors between the current and destination floors
     *
     * @param floorNumber The destination floor number
     * @param isUp        The direction of travel (true = up, false = down)
     * @return the distance between the two floors
     */
    public synchronized int distanceTheFloor(int floorNumber, boolean isUp) {
        return state.handleDistanceTheFloor(floorNumber, isUp);
    }

    /**
     * Adds the specified floor number to the list of destinations
     *
     * @param floorNumber The number of destination floor to add
     * @param isUp        The direction of the elevator
     */
    public synchronized void addDestination(int floorNumber, boolean isUp) {
        state.handleAddDestination(floorNumber, isUp);
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
    }

    interface State {
        /**
         * Turns off the previous lamp and turns on the next one
         */
        void handleSetLamps();

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param floorNumber The destination floor number
         * @param isUp        The direction of travel (true = up, false = down)
         * @return the distance between the two floors
         */
        int handleDistanceTheFloor(int floorNumber, boolean isUp);

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param floorNumber The number of destination floor to add
         * @param isUp        The direction of the elevator
         */
        void handleAddDestination(int floorNumber, boolean isUp);

        /**
         * @return true if the elevator should stop at the next floor
         */
        boolean handleStopForNextFloor();

        /**
         * Actions for when the elevator stops at a floor
         */
        void handleAtFloor() throws IOException, ClassNotFoundException;
    }

    /**
     * Represents a stationary elevator
     */
    class ElevatorNotMoving implements State {
        public ElevatorNotMoving() {
            logger.info("Elevator " + elevatorNumber + " State Changed to: Idle");
        }

        @Override
        public void handleSetLamps() {
            throw new RuntimeException();
        }

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param floorNumber The destination floor number
         * @param isUp        The direction of travel (true = up, false = down)
         * @return the distance between the two floors
         */
        public int handleDistanceTheFloor(int floorNumber, boolean isUp) {
            return abs(floorNumber - currentFloorNumber);
        }

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param floorNumber The number of destination floor to add
         * @param isUp        The direction of the elevator
         */
        public synchronized void handleAddDestination(int floorNumber, boolean isUp) {
            new Thread(arrivalSensor).start();
            destinationsInPath.add(floorNumber);
            state = floorNumber > currentFloorNumber ? new ElevatorMovingUp() : new ElevatorMovingDown();
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
        abstract protected Set<Integer> getWaitingPeople() throws IOException, ClassNotFoundException;

        /**
         * Reverses the direction of travel
         */
        abstract protected void ChangeDirectionOfTravel();

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
            door.open();
            destinationsInPath.remove(currentFloorNumber);
            Set<Integer> destinations = getWaitingPeople();
            destinations.forEach(destination -> buttons.get(destination).setOn(true));
            destinationsInPath.addAll(destinations);
            try {
                Thread.sleep(config.getIntProperty("waitTime"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            door.close();
            if (destinationsInPath.isEmpty()) {
                if (!destinationsOutOfPath.isEmpty()) {
                    ChangeDirectionOfTravel();
                    destinationsInPath.addAll(destinationsOutOfPath);
                    destinationsOutOfPath.clear();

                    //Handle the edge case when the elevator is turning around on the floor where it needs to pick up someone.
                    if (destinationsInPath.contains(currentFloorNumber)) {
                        atFloor();
                    }
                    motor.setMoving(true);
                } else {
                    arrivalSensor.shutDown();
                    state = new ElevatorNotMoving();
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
         * @param floorNumber The destination floor number
         * @param isUp        The direction of travel (true = up, false = down)
         * @return the distance between the two floors
         */
        @Override
        public int handleDistanceTheFloor(int floorNumber, boolean isUp) {
            return abs(floorNumber - currentFloorNumber) +
                    (isUp ? maxFloors - currentFloorNumber : 0);
        }

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param floorNumber The number of destination floor to add
         * @param isUp        The direction of the elevator
         */
        @Override
        public void handleAddDestination(int floorNumber, boolean isUp) {
            if (arrivalSensor.isNotRunning()) {
                new Thread(arrivalSensor).start();
            }
            if (isUp && floorNumber > currentFloorNumber) {
                destinationsInPath.add(floorNumber);
            } else {
                destinationsOutOfPath.add(floorNumber);
            }
        }

        /**
         * @return true if the elevator should stop at the next floor
         */
        @Override
        public boolean handleStopForNextFloor() {
            return destinationsInPath.contains(++currentFloorNumber);
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
        protected Set<Integer> getWaitingPeople() throws IOException, ClassNotFoundException {
            return scheduler.getWaitingPeopleUp(currentFloorNumber);
        }

        /**
         * Reverses the direction of travel
         */
        @Override
        protected void ChangeDirectionOfTravel() {
            state = new ElevatorMovingDown();
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
         * @param floorNumber The destination floor number
         * @param isUp        The direction of travel (true = up, false = down)
         * @return the distance between the two floors
         */
        @Override
        public int handleDistanceTheFloor(int floorNumber, boolean isUp) {
            return abs(floorNumber - currentFloorNumber) + currentFloorNumber;
        }

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param floorNumber The number of destination floor to add
         * @param isUp        The direction of the elevator
         */
        @Override
        public void handleAddDestination(int floorNumber, boolean isUp) {
            if (arrivalSensor.isNotRunning()) {
                new Thread(arrivalSensor).start();
            }
            if (!isUp && floorNumber < currentFloorNumber) {
                destinationsInPath.add(floorNumber);
            } else {
                destinationsOutOfPath.add(floorNumber);
            }
        }

        /**
         * @return true if the elevator should stop at the next floor
         */
        @Override
        public boolean handleStopForNextFloor() {
            return destinationsInPath.contains(--currentFloorNumber);
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
        protected Set<Integer> getWaitingPeople() throws IOException, ClassNotFoundException {
            return scheduler.getWaitingPeopleDown(currentFloorNumber);
        }

        /**
         * Reverses the direction of travel
         */
        @Override
        protected void ChangeDirectionOfTravel() {
            state = new ElevatorMovingUp();
        }


    }
}

