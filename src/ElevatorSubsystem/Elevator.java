package ElevatorSubsystem;

import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

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
    private final ArrayList<ElevatorButton> buttons;
    private final ArrayList<ElevatorLamp> elevatorLamps;
    private boolean idle;
    private ArrayList<Floor> destinationFloors;
    private int currentFloorNumber;

    /**
     * Constructor for Elevator
     *
     * @param scheduler The system scheduler
     */
    public Elevator(Scheduler scheduler) {
        idle = true;
        this.scheduler = scheduler;
        door = new Door();
        arrivalSensor = new ArrivalSensor();
        motor = new Motor();
        buttons = new ArrayList<>();
        elevatorLamps = new ArrayList<>();
        destinationFloors = new ArrayList<>();
        currentFloorNumber = 0;
        motor.setMoving(false);

        for (int i = 0; i < scheduler.floors.values().size(); i++) {
            buttons.add(new ElevatorButton(i + 1));
            elevatorLamps.add(new ElevatorLamp());
        }
    }


    /**
     * Moves the elevator to the specified floor and notifies the scheduler
     *
     * @param destinationFloorNumber The destination floor
     */
    public synchronized void moveToFloorNumber(int destinationFloorNumber) {
        while (motor.isMoving()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        idle = false;
        while (currentFloorNumber != destinationFloorNumber) {

            idle = false;
            motor.setMoving(true);
            motor.setDirectionsIsUp(destinationFloorNumber > currentFloorNumber);

            moveFloor(destinationFloorNumber);
        }
        motor.setMoving(false);
        door.setOpen(true);
        idle = true;
        notifyAll();
    }

    public synchronized void moveFloor(int finalDestination) {
        int originalFloor = currentFloorNumber;

        if (motor.directionsIsUp()) {
            currentFloorNumber += 1;
        } else {
            currentFloorNumber -= 1;
        }
        System.out.println("Moving elevator to " + currentFloorNumber);
        arrivalSensor.callOnArrival(originalFloor, currentFloorNumber);

        if (currentFloorNumber == finalDestination || destinationFloors.contains(scheduler.floors.get(currentFloorNumber))) {
            System.out.println("elevator arrived at " + currentFloorNumber);
            openDoors(finalDestination, motor.directionsIsUp());
            scheduler.elevatorArrivedAtFloorNumber(currentFloorNumber);
            if (originalFloor != 0 && destinationFloors.contains(scheduler.floors.get(currentFloorNumber))) {
                destinationFloors.remove(scheduler.floors.get(currentFloorNumber));
            }
        }

        if (arrivalSensor.isRequest(scheduler.floors.get(currentFloorNumber), motor.directionsIsUp())) {
            System.out.println("elevator arrived at " + currentFloorNumber);
            openDoors(currentFloorNumber, motor.directionsIsUp());
            scheduler.elevatorArrivedAtFloorNumber(currentFloorNumber);
            Floor floor = scheduler.floors.get(currentFloorNumber);
            int dest = floor.getSchedule().peek().getCarButton();
            System.out.println(destinationFloors.contains(scheduler.floors.get(dest)));
            if (finalDestination != dest) {
                destinationFloors.add(scheduler.floors.get(dest));
                scheduler.removeEvent(floor.getSchedule().peek());
                scheduler.moveElevatorToFloorNumber(currentFloorNumber, dest);
            } else {
                scheduler.removeEvent(floor.getSchedule().peek());
            }
            floor.getSchedule().poll();
        }
    }


    public void openDoors(int floor, boolean isUp) {
        door.setOpen(true);
        if (isUp) {
            scheduler.floors.get(floor).turnUpButtonOff();
        } else {
            scheduler.floors.get(floor).turnDownButtonOff();
        }
        System.out.println("doors open at floor " + floor);
    }

    /**
     * Closes the elevator doors
     */
    public synchronized void closeDoors(int floor) {
        try {
            wait(WAIT_TIME * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doors closed at floor " + floor);

        door.setOpen(false);
        buttons.get(floor).setOn(false);
        elevatorLamps.get(floor).setLamp(false);
    }

    /**
     * Getter for the current floor number
     *
     * @return The current floor number
     */
    public synchronized int getCurrentFloorNumber() {
        return currentFloorNumber;
    }

    public boolean getIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public ArrayList<ElevatorButton> getButtons() {
        return buttons;
    }

    public ArrayList<ElevatorLamp> getElevatorLamps() {
        return elevatorLamps;
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        while (scheduler.hasEvents()) {
        }
    }
}
