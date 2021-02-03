package ElevatorSubsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArrivalSensor {
    private final ScheduledExecutorService executor;

    public ArrivalSensor() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public static long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return destinationFloorNumber - currentFloorNumber; //FIXME This needs to be replaced
    }


    public void callOnArrival(Runnable callback, int currentFloorNumber, int destinationFloorNumber) {
        executor.schedule(callback, ArrivalSensor.getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber), TimeUnit.SECONDS);
    }
}
