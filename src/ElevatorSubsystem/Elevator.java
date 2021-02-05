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

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
        door = new Door();
        arrivalSensor = new ArrivalSensor();
        motor = new Motor();
        buttons = new ArrayList<>();
        elevatorLamps = new ArrayList<>();
        currentFloorNumber = 0;
    }

    public void moveToFloorNumber(int destinationFloorNumber) {
        motor.setDirectionsIsUp(destinationFloorNumber > currentFloorNumber);
        motor.setMoving(true);
        System.out.println("moving elevator to " + destinationFloorNumber);
        arrivalSensor.callOnArrival(() -> {
            currentFloorNumber = destinationFloorNumber;
            scheduler.elevatorArrivedAtFloorNumber(destinationFloorNumber);
            System.out.println("elevator arrived " + destinationFloorNumber);
            motor.setMoving(false);
            scheduler.shutdown();
        }, currentFloorNumber, destinationFloorNumber);
    }

    public boolean isMoving() {
        return motor.isMoving();
    }

    public void shutdown() {
        arrivalSensor.shutdown();
    }

    @Override
    public void run() {
    }
}
