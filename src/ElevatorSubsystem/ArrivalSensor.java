package ElevatorSubsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArrivalSensor {
    private final ScheduledExecutorService executor;
    private static final long DISTANCE_BETWEEN_FLOORS = (long) 3.5;
    private static final long VELOCITY = (long) 1.27;

    public ArrivalSensor() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public static long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return ((destinationFloorNumber - currentFloorNumber)*DISTANCE_BETWEEN_FLOORS)/VELOCITY;
    }


    public synchronized void callOnArrival(Runnable callback, int currentFloorNumber, int destinationFloorNumber) {
        executor.schedule(callback, ArrivalSensor.getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber), TimeUnit.SECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
