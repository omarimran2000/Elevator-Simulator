package ElevatorSubsystem;

import static java.lang.Math.abs;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 06, 2021
 */
public class ArrivalSensor implements Runnable {
    private static final long DISTANCE_BETWEEN_FLOORS = (long) 3.5;
    private static final long VELOCITY = (long) 1.27;
    private final Elevator elevator;
    private boolean run;

    /**
     * Constructor for ArrivalSensor
     * Instantiates a ScheduledExecutorService
     *
     * @param elevator
     */
    public ArrivalSensor(Elevator elevator) {
        this.elevator = elevator;
        run = false;
    }

    /**
     * Method to determine travel time between two floors
     *
     * @param currentFloorNumber     The current floor
     * @param destinationFloorNumber The destination floor
     * @return The time in seconds to travel between the two floors
     */
    public static long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return (abs(destinationFloorNumber - currentFloorNumber) * DISTANCE_BETWEEN_FLOORS) / VELOCITY;
    }


    public boolean isRunning() {
        return run;
    }


    public void shutDown() {
        run = false;
    }

    @Override
    public void run() {
        run = true;
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //FIXME add physics
            if (elevator.stopForNextFloor()) {
                elevator.atFloor();
            } else {
                elevator.passFloor();
            }
        }
    }
}
