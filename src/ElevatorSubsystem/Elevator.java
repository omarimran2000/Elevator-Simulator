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
     * @param destinationFloorNumber The destination floor
     */
    public void moveToFloorNumber(int destinationFloorNumber) {
        System.out.println("moving elevator to " + destinationFloorNumber);
        arrivalSensor.callOnArrival(() -> {
            currentFloorNumber = destinationFloorNumber;
            scheduler.elevatorArrivedAtFloorNumber(destinationFloorNumber);
            System.out.println("elevator arrived " + destinationFloorNumber);
        }, currentFloorNumber, destinationFloorNumber);
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        while(scheduler.getNumEvents()>0)
        {

        }
        arrivalSensor.shutdown();
    }
}
