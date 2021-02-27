package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.abs;

/**
 * The Elevator class represents a single elevator in the system
 *
 * @version Feb 06, 2021
 */
public class Elevator implements Runnable {

    private State state;

    protected static final long WAIT_TIME = (long) 9.175;
    protected final Scheduler scheduler;

    protected final Door door;
    protected final ArrivalSensor arrivalSensor;
    protected final Motor motor;
    protected final Map<Integer, ElevatorButton> buttons;
    protected final Map<Integer, ElevatorLamp> lamps;

    protected final Set<Integer> destinationsInPath;
    protected final Set<Integer> destinationsOutOfPath;
    protected final int maxFloors;
    protected int currentFloorNumber;

    /**
     * Constructor for Elevator
     *
     * @param scheduler The system scheduler
     */
    public Elevator(int elevatorNumber, Scheduler scheduler, int maxFloors) {
        this.scheduler = scheduler;
        this.maxFloors = maxFloors;
        door = new Door();
        arrivalSensor = new ArrivalSensor(this);
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

    public Set<Integer> getDestinationPath(){
        return destinationsInPath;
    }

    /**
     * The run method
     */
    @Override
    public void run() {
    }

    public int distanceTheFloor(int floorNumber, boolean isUp) {
        return state.handleDistanceTheFloor(floorNumber, isUp);
    }

    public synchronized void addDestination(int floorNumber, boolean isUp) {
        state.handleAddDestination(floorNumber, isUp);
    }

    public synchronized boolean stopForNextFloor() {
        return state.handleStopForNextFloor();
    }

    public synchronized void passFloor() {
        state.handleSetLamps();
        System.out.println("Elevator passing floor " + currentFloorNumber);
    }


    public synchronized void atFloor() {
        state.handleAtFloor();
    }

    interface State {
        void handleSetLamps();

        int handleDistanceTheFloor(int floorNumber, boolean isUp);

        void handleAddDestination(int floorNumber, boolean isUp);

        boolean handleStopForNextFloor();

        void handleAtFloor();
    }

    class ElevatorNotMoving implements State {
        @Override
        public void handleSetLamps() {
            throw new RuntimeException();
        }

        public int handleDistanceTheFloor(int floorNumber, boolean isUp) {
            return abs(floorNumber - currentFloorNumber);
        }

        public synchronized void handleAddDestination(int floorNumber, boolean isUp) {
            new Thread(arrivalSensor).start();
            destinationsInPath.add(floorNumber);
            state = floorNumber > currentFloorNumber ? new ElevatorMovingUp() : new ElevatorMovingDown();
        }

        @Override
        public boolean handleStopForNextFloor() {
            throw new RuntimeException();
        }

        @Override
        public void handleAtFloor() {
            throw new RuntimeException();
        }
    }

    abstract class MovingState implements State {

        abstract protected ElevatorLamp getPreviousLamp();

        abstract protected Set<Integer> getWaitingPeople();

        abstract protected void ChangeDirectionOfTravel();

        @Override
        public void handleSetLamps() {
            ElevatorLamp previousLamp = getPreviousLamp();
            if (previousLamp != null && previousLamp.isLit()) {
                previousLamp.setLamp(false);
            }
            lamps.get(currentFloorNumber).setLamp(true);
        }

        @Override
        public void handleAtFloor() {
            handleSetLamps();
            System.out.println("Elevator stopped at floor " + currentFloorNumber);
            motor.setMoving(false);
            door.open();
            destinationsInPath.remove(currentFloorNumber);
            destinationsInPath.addAll(getWaitingPeople());
            try {
                Thread.sleep(WAIT_TIME * 1000);
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
                } else {
                    arrivalSensor.shutDown();
                    state = new ElevatorNotMoving();
                }
            }
            motor.setMoving(true);
        }
    }

    class ElevatorMovingUp extends MovingState {

        @Override
        public int handleDistanceTheFloor(int floorNumber, boolean isUp) {
            return abs(floorNumber - currentFloorNumber) +
                    (isUp ? maxFloors - currentFloorNumber : 0);
        }

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

        @Override
        public boolean handleStopForNextFloor() {
            return destinationsInPath.contains(++currentFloorNumber);
        }

        @Override
        protected ElevatorLamp getPreviousLamp() {
            return lamps.get(currentFloorNumber - 1);
        }

        @Override
        protected Set<Integer> getWaitingPeople() {
            return scheduler.getWaitingPeopleUp(currentFloorNumber);
        }

        @Override
        protected void ChangeDirectionOfTravel() {
            state = new ElevatorMovingDown();
        }
    }

    private class ElevatorMovingDown extends MovingState {
        @Override
        public int handleDistanceTheFloor(int floorNumber, boolean isUp) {
            return abs(floorNumber - currentFloorNumber) + currentFloorNumber;
        }

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

        @Override
        public boolean handleStopForNextFloor() {
            return destinationsInPath.contains(--currentFloorNumber);
        }

        @Override
        protected ElevatorLamp getPreviousLamp() {
            return lamps.get(currentFloorNumber + 1);
        }

        @Override
        protected Set<Integer> getWaitingPeople() {
            return scheduler.getWaitingPeopleDown(currentFloorNumber);
        }

        @Override
        protected void ChangeDirectionOfTravel() {
            state = new ElevatorMovingUp();
        }
    }
}

