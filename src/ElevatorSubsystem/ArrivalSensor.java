package ElevatorSubsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArrivalSensor {
    private final ScheduledExecutorService executor;

    /**
     * Constructor for ArrivalSensor
     * Instantiates a ScheduledExecutorService
     */
    public ArrivalSensor() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Method to determine travel time between two floors
     * @param currentFloorNumber The current floor
     * @param destinationFloorNumber The destination floor
     * @return The time in seconds to travel between the two floors
     */
    public static long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return destinationFloorNumber - currentFloorNumber; //FIXME This needs to be replaced
    }

    /**
     *
     * @param callback
     * @param currentFloorNumber The number of the current floor
     * @param destinationFloorNumber The number of the destination floor
     */
    public void callOnArrival(Runnable callback, int currentFloorNumber, int destinationFloorNumber) {
        executor.schedule(callback, ArrivalSensor.getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber), TimeUnit.SECONDS);
    }

    /**
     * Shuts down the executor
     */
    public void shutdown() {
        executor.shutdown();
    }
}
