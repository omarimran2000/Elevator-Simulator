package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

public class Elevator implements Runnable {

    private final Scheduler scheduler;
    private final Door door;
    private final ArrivalSensor arrivalSensor;
    private final Motor motor;
    private boolean idle;
    private final ArrayList<ElevatorButton> buttons;
    private final ArrayList<ElevatorLamp> elevatorLamps;
    private int currentFloorNumber;
    private static final long WAIT_TIME = (long) 9.175;

    public Elevator(Scheduler scheduler) {
        idle = true;
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
        idle = false;
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

        notifyAll();

    }
    public void openDoors(int floor){

        System.out.println("doors open at floor "+floor);
    }

    public synchronized void closeDoors(int floor){
        try {
            wait(WAIT_TIME * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doors closed at floor "+floor);
        door.setOpen(false);
    }

    public int getCurrentFloorNumber(){
        return currentFloorNumber;
    }

    public void setIdle(boolean idle)
    {
        this.idle = idle;
    }
    public boolean getIdle()
    {
        return idle;
    }
    public Motor getMotor()
    {
        return motor;
    }
    @Override
    public void run() {
        while(scheduler.hasEvents())
        {

        }
    }
}
