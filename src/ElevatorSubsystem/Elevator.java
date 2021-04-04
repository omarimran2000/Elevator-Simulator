package ElevatorSubsystem;

import GUI.BufferedGUI;
import GUI.GuiApi;
import SchedulerSubsystem.SchedulerApi;
import model.AckMessage;
import model.Destination;
import model.ElevatorState;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The Elevator class represents a single elevator in the system
 *
 * @version Feb 27, 2021
 */
public class Elevator extends Thread implements ElevatorApi {

    protected final SchedulerApi scheduler;
    protected final BufferedGUI gui;
    protected final Config config;
    protected final Door door;
    protected final ArrivalSensor arrivalSensor;
    protected final Motor motor;
    protected final Map<Integer, ElevatorButton> buttons;
    protected final Map<Integer, ElevatorLamp> lamps;
    protected final Set<Destination> destinations;
    protected final Set<Integer> people;
    protected final int elevatorNumber;
    protected final int maxFloors;
    private final ScheduledExecutorService executor;
    private final Logger logger;
    private final DatagramSocket socket;
    private final Destination position;
    private final Thread GUISendThread;
    private State state;
    private int idleDestination;
    private boolean idleWrongDirection;

    /**
     * Constructor for Elevator
     *
     * @param config
     * @param scheduler The system scheduler
     * @param gui
     */
    public Elevator(Config config, SchedulerApi scheduler, GuiApi gui, int elevatorNumber, int maxFloors) throws IOException, ClassNotFoundException {
        this.config = config;
        this.scheduler = scheduler;
        this.gui = new BufferedGUI(gui);
        this.maxFloors = maxFloors;
        this.elevatorNumber = elevatorNumber;
        logger = Logger.getLogger(this.getClass().getName());
        door = new Door(elevatorNumber, config, this.gui);
        arrivalSensor = new ArrivalSensor(config, this);
        motor = new Motor(elevatorNumber, this.gui);
        buttons = new HashMap<>();
        lamps = new HashMap<>();
        position = new Destination(0, true);
        state = new ElevatorNotMoving();

        for (int i = 0; i <= maxFloors; i++) {
            buttons.put(i, new ElevatorButton(i));
            lamps.put(i, new ElevatorLamp(elevatorNumber, i));
        }
        destinations = new HashSet<>();
        people = new HashSet<>();
        socket = new DatagramSocket(config.getIntProperty("elevatorPort") + elevatorNumber);
        executor = Executors.newSingleThreadScheduledExecutor();
        GUISendThread = new Thread(this.gui);
    }

    /**
     * Getter for the current floor number
     *
     * @return The current floor number
     */
    public synchronized int getCurrentFloorNumber() {
        return position.getFloorNumber();
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
        GUISendThread.start();
        try {
            StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                    1, input -> distanceTheFloor((Destination) input.get(0)),
                    2, input -> addDestination((Destination) input.get(0)),
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
        executor.shutdown();
        arrivalSensor.interrupt();
        GUISendThread.interrupt();
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
        return state.distanceTheFloor(destination);
    }

    /**
     * Adds the specified floor number to the list of destinations
     *
     * @param destination The new destination for the Elevator
     * @return
     */
    @Override
    public synchronized boolean addDestination(Destination destination) {
        if (state.getElevatorState() == ElevatorState.NotMoving ||
                (destination.isUp() == position.isUp()) && destination.getFloorNumber() > position.getFloorNumber() == position.isUp()) {
            state.addDestination(destination);
            gui.setElevatorButton(elevatorNumber, destination.getFloorNumber(), false, true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if the elevator should stop at the next floor
     */
    public synchronized boolean stopForNextFloor() {
        boolean b = state.stopForNextFloor();
        gui.setCurrentFloorNumber(elevatorNumber, position.getFloorNumber());
        return b;
    }

    /**
     * Pass the floor without stopping
     */
    public synchronized void passFloor() {
        state.setLamps();
        logger.info("Elevator " + elevatorNumber + " passing floor " + position.getFloorNumber());

        if (elevatorNumber == config.getIntProperty("elevatorStuck")) scheduleCheckIfStuck();
    }

    /**
     * Actions for when the elevator stops at a floor
     */
    public synchronized void atFloor() {
        try {
            state.atFloor();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (elevatorNumber == config.getIntProperty("elevatorStuck")) scheduleCheckIfStuck();
    }

    private void scheduleCheckIfStuck() {
        executor.schedule(this::checkIfStuck, config.getIntProperty("checkIfStuckDelay"), TimeUnit.SECONDS);
    }

    /**
     * Used to check if the elevator is stuck
     */
    private void checkIfStuck() {
        if (arrivalSensor.isStuck()) {
            synchronized (this) {
                logger.warning("Elevator" + elevatorNumber + " is stuck");
                state = new ElevatorStuck();
            }
            for (Destination destination : destinations) {
                try {
                    scheduler.handleFloorButton(destination);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                gui.setElevatorButton(elevatorNumber, destination.getFloorNumber(), true, false);
            }
            destinations.clear();
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
        void setLamps();

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        int distanceTheFloor(Destination destination);

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param destination The new destination for the Elevator
         */
        void addDestination(Destination destination);

        /**
         * @return true if the elevator should stop at the next floor
         */
        boolean stopForNextFloor();

        /**
         * Actions for when the elevator stops at a floor
         */
        void atFloor() throws IOException, ClassNotFoundException;

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
            gui.setState(elevatorNumber, getElevatorState());
        }

        @Override
        public void setLamps() {
            throw new RuntimeException();
        }

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        @Override
        public int distanceTheFloor(Destination destination) {
            return Math.abs(destination.getFloorNumber() - position.getFloorNumber());
        }

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param destination The new destination for the Elevator
         */
        @Override
        public synchronized void addDestination(Destination destination) {
            arrivalSensor.start();
            destinations.add(destination);
            state = new MovingState();
            position.setUp(destination.getFloorNumber() > position.getFloorNumber());
            idleDestination = destination.getFloorNumber();
            idleWrongDirection = destination.isUp() != position.isUp();
        }

        /**
         * @return true if the elevator should stop at the next floor
         */
        @Override
        public boolean stopForNextFloor() {
            throw new RuntimeException();
        }

        /**
         * Actions for when the elevator stops at a floor
         */
        @Override
        public void atFloor() {
            throw new RuntimeException();
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
    class MovingState implements State {

        public MovingState() {
            motor.setMoving(position.isUp());
            gui.setState(elevatorNumber, getElevatorState());
        }

        /**
         * Gets the number of floors between the current and destination floors
         *
         * @param destination Potential destination for the elevator
         * @return the distance between the two floors
         */
        @Override
        public int distanceTheFloor(Destination destination) {
            return Math.abs(destination.getFloorNumber() - position.getFloorNumber()) + destinations.size() * 10 + people.size() * 5;
        }

        /**
         * Adds the specified floor number to the list of destinations
         *
         * @param destination The new destination for the Elevator
         */
        @Override
        public void addDestination(Destination destination) {
            if (arrivalSensor.isNotRunning()) {
                arrivalSensor.start();
            }
            destinations.add(destination);
            if ((position.isUp() && destination.getFloorNumber() > idleDestination) || (!position.isUp() && destination.getFloorNumber() < idleDestination)) {
                idleDestination = destination.getFloorNumber();
            }
            idleWrongDirection = destination.isUp() != position.isUp() || idleWrongDirection;
        }

        @Override
        public boolean stopForNextFloor() {
            position.setFloorNumber(position.getFloorNumber() + (position.isUp() ? 1 : -1));
            return people.contains(position.getFloorNumber()) || destinations.contains(position) || position.getFloorNumber() == idleDestination;
        }

        /**
         * Turns off the previous lamp and turns on the next one
         */
        @Override
        public void setLamps() {
            ElevatorLamp previousLamp = lamps.get(position.getFloorNumber() + (position.isUp() ? -1 : 1));
            if (previousLamp != null && previousLamp.isLit()) {
                previousLamp.setLamp(false);
            }
            lamps.get(position.getFloorNumber()).setLamp(true);
        }

        /**
         * Actions for when the elevator stops at a floor
         */
        @Override
        public void atFloor() throws IOException, ClassNotFoundException {
            setLamps();
            logger.info("Elevator " + elevatorNumber + " stopped at floor " + position.getFloorNumber());
            motor.setMoving(false);

            while (!door.isOpen()) {
                door.open();
                if (!door.isOpen()) {
                    logger.warning("Elevator " + elevatorNumber + " doors stuck closed at floor " + position.getFloorNumber());
                } else {
                    gui.setDoorsStuck(elevatorNumber, true, false);
                }
            }
            gui.setDoorsStuck(elevatorNumber, false, false);

            if (idleDestination == position.getFloorNumber() && idleWrongDirection) {
                idleWrongDirection = false;
                position.setUp(!position.isUp());
                motor.setDirectionIsUp(position.isUp());
                gui.setState(elevatorNumber, getElevatorState());
            }
            gui.setElevatorButton(elevatorNumber, position.getFloorNumber(), false, false);

            people.remove(position.getFloorNumber());
            destinations.remove(position);

            Set<Integer> floors = scheduler.getWaitingPeople(position);
            floors.forEach(destination -> {
                buttons.get(destination).setOn(true);
                gui.setElevatorButton(elevatorNumber, destination, true, true);
            });

            people.addAll(floors);
            if (!people.isEmpty()) {
                idleDestination = position.isUp() ? Math.max(idleDestination, Collections.max(people)) : Math.min(idleDestination, Collections.min(people));
            }

            while (door.isOpen()) {
                door.close();
                if (door.isOpen()) {
                    logger.warning("Elevator " + elevatorNumber + " doors stuck open at floor " + position.getFloorNumber());
                    gui.setDoorsStuck(elevatorNumber, true, true);
                } else {
                    gui.setDoorsStuck(elevatorNumber, false, true);
                }
            }

            if (destinations.isEmpty() && people.isEmpty()) {
                destinations.addAll(scheduler.getUnscheduledPeople(position.getFloorNumber()));
                if (destinations.isEmpty()) {
                    arrivalSensor.interrupt();
                    state = new ElevatorNotMoving();
                } else {
                    position.setUp(destinations.stream().anyMatch(destination -> destination.getFloorNumber() > position.getFloorNumber()));
                    idleDestination = position.getFloorNumber();
                    for (Destination destination : destinations) {
                        gui.setElevatorButton(elevatorNumber, destination.getFloorNumber(), false, true);
                        if ((position.isUp() && destination.getFloorNumber() > idleDestination) || (!position.isUp() && destination.getFloorNumber() < idleDestination)) {
                            idleDestination = destination.getFloorNumber();
                        }
                    }
                    idleWrongDirection = destinations.stream().anyMatch(destination -> destination.isUp() != position.isUp());
                    motor.setDirectionIsUp(position.isUp());
                    gui.setState(elevatorNumber, getElevatorState());

                    //Handle the edge case when the elevator is turning around on the floor where it needs to pick up someone.
                    if (destinations.contains(position)) {
                        this.atFloor();
                    }
                    motor.setMoving(true);
                }
            } else {
                motor.setMoving(true);
            }
        }

        @Override
        public ElevatorState getElevatorState() {
            return position.isUp() ? ElevatorState.MovingUp : ElevatorState.MovingDown;
        }
    }

    private class ElevatorStuck implements State {
        public ElevatorStuck() {
            arrivalSensor.interrupt();
            motor.setMoving(false);
            gui.setState(elevatorNumber, getElevatorState());
        }

        @Override
        public void setLamps() {
            throw new RuntimeException();
        }

        @Override
        public int distanceTheFloor(Destination destination) {
            throw new RuntimeException();
        }

        @Override
        public void addDestination(Destination destination) {
            throw new RuntimeException();
        }

        @Override
        public boolean stopForNextFloor() {
            return false;
        }

        @Override
        public void atFloor() {
            throw new RuntimeException();
        }

        @Override
        public ElevatorState getElevatorState() {
            return ElevatorState.Stuck;
        }
    }
}
