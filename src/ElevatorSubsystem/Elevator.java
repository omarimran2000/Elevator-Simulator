package ElevatorSubsystem;
import SchedulerSubsystem.Scheduler;
import java.util.ArrayList;

/**
 * The Elevator class represents a single elevator in the system
 * @version Feb 06, 2021
 */
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
        currentFloorNumber = 0;
        motor.setMoving(false);

        for(int i=0;i<scheduler.floors.values().size();i++)
        {
            buttons.add(new ElevatorButton(i+1));
            elevatorLamps.add(new ElevatorLamp());
        }
    }


    /**
     * Moves the elevator to the specified floor and notifies the scheduler
     *
     * @param destinationFloorNumber The destination floor
     */
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
        System.out.println("Moving elevator to "+destinationFloorNumber);
        motor.setMoving(true);

        arrivalSensor.callOnArrival(currentFloorNumber,destinationFloorNumber);
        currentFloorNumber = destinationFloorNumber;

        System.out.println("elevator arrived " + destinationFloorNumber);
        motor.setMoving(false);
        door.setOpen(true);
        openDoors(destinationFloorNumber);
        scheduler.elevatorArrivedAtFloorNumber(destinationFloorNumber);

        notifyAll();

    }

    public void openDoors(int floor){

        System.out.println("doors open at floor "+floor);
    }

    /**
     * Closes the elevator doors
     */
    public synchronized void closeDoors(int floor){
        try {
            wait(WAIT_TIME * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doors closed at floor "+floor);

        door.setOpen(false);
        buttons.get(floor).setOn(false);
        elevatorLamps.get(floor).setLamp(false);
    }

    /**
     * Getter for the current floor number
     * @return The current floor number
     */
    public int getCurrentFloorNumber() {
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
        while(scheduler.hasEvents())
        {

        }
    }
}
