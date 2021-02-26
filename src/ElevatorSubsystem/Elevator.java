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

    private static final long WAIT_TIME = (long) 9.175;
    private final Scheduler scheduler;

    private final Door door;
    private final ArrivalSensor arrivalSensor;
    private final Motor motor;
    private final Map<Integer, ElevatorButton> buttons;
    private final Map<Integer, ElevatorLamp> lamps;

    private final Set<Integer> destinationsInPath;
    private final Set<Integer> destinationsOutOfPath;
    private final int maxFloors;
    private int currentFloorNumber;

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
        motor.setMoving(false);

        for (int i = 1; i <= maxFloors; i++) {
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
     * The run method
     */
    @Override
    public void run() {
    }

    public int distanceTheFloor(int floorNumber, boolean isUp) {
        return abs(floorNumber - currentFloorNumber) +
                (isUp == motor.directionIsUp() ? isUp ?
                        maxFloors - currentFloorNumber :
                        currentFloorNumber :
                        0);
    }

    private void setLamps() {
        ElevatorLamp previousLamp = lamps.get(currentFloorNumber + (motor.directionIsUp() ? -1 : 1));
        if (previousLamp != null && previousLamp.isLit()) {
            previousLamp.setLamp(false);
        }
        lamps.get(currentFloorNumber).setLamp(true);

    }

    public synchronized void addDestination(int floorNumber, boolean isUp) {
        if (!arrivalSensor.isRunning()) {
            new Thread(arrivalSensor).start();
        }
        if (isUp == motor.directionIsUp() &&
                (motor.directionIsUp() && floorNumber > currentFloorNumber ||
                        !motor.directionIsUp() && floorNumber < currentFloorNumber)) {
            destinationsInPath.add(floorNumber);
        } else {
            destinationsOutOfPath.add(floorNumber);
        }
    }

    public synchronized boolean stopForNextFloor() {
        return destinationsInPath.contains(motor.directionIsUp() ? ++currentFloorNumber : --currentFloorNumber);
    }

    public synchronized void passFloor() {
        setLamps();
        System.out.println("Elevator passing floor " + currentFloorNumber);
    }


    public synchronized void atFloor() {
        setLamps();
        System.out.println("Elevator stopped at floor " + currentFloorNumber);
        motor.setMoving(false);
        door.open();
        destinationsInPath.remove(currentFloorNumber);
        destinationsInPath.addAll(motor.directionIsUp() ?
                scheduler.getWaitingPeopleUp(currentFloorNumber) :
                scheduler.getWaitingPeopleDown(currentFloorNumber));
        try {
            Thread.sleep(WAIT_TIME * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        door.close();
        if (destinationsInPath.isEmpty()) {
            if (!destinationsOutOfPath.isEmpty()) {
                motor.setDirectionIsUp(!motor.directionIsUp());
                destinationsInPath.addAll(destinationsOutOfPath);
                destinationsOutOfPath.clear();

                //Handle the edge case when the elevator is turning around on the floor where it needs to pick up someone.
                if (destinationsInPath.contains(currentFloorNumber)) {
                    atFloor();
                }
            } else {
                arrivalSensor.shutDown();
            }
        }
        motor.setMoving(true);
    }
}
