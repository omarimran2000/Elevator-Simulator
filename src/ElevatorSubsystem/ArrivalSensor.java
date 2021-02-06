package ElevatorSubsystem;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class ArrivalSensor {
    //private final ScheduledExecutorService executor;
    private static final long DISTANCE_BETWEEN_FLOORS = (long) 3.5;
    private static final long VELOCITY = (long) 1.27;

    /**
     * Constructor for ArrivalSensor
     * Instantiates a ScheduledExecutorService
     */
    public ArrivalSensor() {
        //executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Method to determine travel time between two floors
     * @param currentFloorNumber The current floor
     * @param destinationFloorNumber The destination floor
     * @return The time in seconds to travel between the two floors
     */
    public static long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return (abs(destinationFloorNumber - currentFloorNumber)*DISTANCE_BETWEEN_FLOORS)/VELOCITY;
    }


    /**
     *
     * @param currentFloorNumber The number of the current floor
     * @param destinationFloorNumber The number of the destination floor
     */

    public synchronized void callOnArrival(int currentFloorNumber, int destinationFloorNumber){
        //executor.schedule(callback, ArrivalSensor.getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber), TimeUnit.SECONDS);
        try {
            wait(getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber)*1000);
        }catch(InterruptedException ex)
        {
            ex.getStackTrace();
        }


    }

    /**
     * Shuts down the executor
     */
    public void shutdown() {
        //executor.shutdown();
    }
}
