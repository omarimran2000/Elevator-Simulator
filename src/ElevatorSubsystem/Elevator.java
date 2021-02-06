package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

public class Elevator implements Runnable {

    private final Scheduler scheduler;
    private final Door door;
    private final ArrivalSensor arrivalSensor;
    private final Motor motor;
    private final ArrayList<ElevatorButton> buttons;
    private final ArrayList<ElevatorLamp> elevatorLamps;
    private int currentFloorNumber;

    /**
     * Constructor for Elevator
     *
     * @param scheduler The system scheduler
     */
    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
        door = new Door();
        arrivalSensor = new ArrivalSensor();
        motor = new Motor();
        buttons = new ArrayList<>();
        elevatorLamps = new ArrayList<>();
        currentFloorNumber = 0;
    }

    /**
     * Moves the elevator to the specified floor and notifies the scheduler
     *
     * @param destinationFloorNumber The destination floor
     */
    //public void moveToFloorNumber(int destinationFloorNumber) {
    public synchronized void moveToFloorNumber(int destinationFloorNumber) {
        motor.setDirectionsIsUp(destinationFloorNumber > currentFloorNumber);
        motor.setMoving(true);
        System.out.println("moving elevator to " + destinationFloorNumber);
        arrivalSensor.callOnArrival(() -> {
            currentFloorNumber = destinationFloorNumber;
            scheduler.elevatorArrivedAtFloorNumber(destinationFloorNumber);
            System.out.println("elevator arrived " + destinationFloorNumber);
            door.setOpen(true);
            openDoors();

            motor.setMoving(false);
            scheduler.shutdown();
        }, currentFloorNumber, destinationFloorNumber);

    }

    /**
     * Opens the elevator doors
     */
    public void openDoors() {
        System.out.println("doors open");
    }

    /**
     * Closes the elevator doors
     */
    public void closeDoors() {
        System.out.println("doors closed");
        door.setOpen(false);
    }

    /**
     * Getter for the current floor number
     * @return The current floor number
     */
    public int getCurrentFloorNumber() {
        return currentFloorNumber;
    }

    /**
     * @return true if the elevator is moving
     */
    public boolean isMoving() {
        return motor.isMoving();
    }

    /**
     * shutdown the arrival sensor
     */
    public void shutdown() {
        arrivalSensor.shutdown();
    }

    /**
     * The run method
     */
    @Override
    public void run() {
    }
}
