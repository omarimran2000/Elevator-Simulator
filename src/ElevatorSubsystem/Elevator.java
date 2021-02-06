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
    private static final long WAIT_TIME = (long) 9.175;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
        door = new Door();
        arrivalSensor = new ArrivalSensor();
        motor = new Motor();
        buttons = new ArrayList<>();
        elevatorLamps = new ArrayList<>();
        currentFloorNumber = 0;
        motor.setMoving(false);
    }

    public synchronized void moveToFloorNumber(int destinationFloorNumber){
        while(motor.isMoving())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        motor.setDirectionsIsUp(destinationFloorNumber > currentFloorNumber);
        motor.setMoving(true);
        System.out.println("moving elevator to " + destinationFloorNumber);
        arrivalSensor.callOnArrival(currentFloorNumber,destinationFloorNumber);
        currentFloorNumber = destinationFloorNumber;

        System.out.println("elevator arrived " + destinationFloorNumber);
        door.setOpen(true);
        openDoors(destinationFloorNumber);
        motor.setMoving(false);
        scheduler.elevatorArrivedAtFloorNumber(destinationFloorNumber);

        motor.setMoving(false);
        notifyAll();

    }
    public void openDoors(int floor){
        System.out.println("doors open at floor "+floor);
    }

    public void closeDoors(int floor){
        System.out.println("doors closed at floor "+floor);
        door.setOpen(false);
    }

    public int getCurrentFloorNumber(){
        return currentFloorNumber;
    }


    public boolean isMoving() {
        return motor.isMoving();
    }

    public void shutdown() {
        arrivalSensor.shutdown();
    }

    @Override
    public void run() {
        while(scheduler.hasEvents())
        {

        }
    }
}
