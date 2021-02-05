package ElevatorSubsystem;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class ArrivalSensor {
    //private final ScheduledExecutorService executor;
    private static final long DISTANCE_BETWEEN_FLOORS = (long) 3.5;
    private static final long VELOCITY = (long) 1.27;

    public ArrivalSensor() {
        //executor = Executors.newSingleThreadScheduledExecutor();
    }

    public static long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return (abs(destinationFloorNumber - currentFloorNumber)*DISTANCE_BETWEEN_FLOORS)/VELOCITY;
    }


    public synchronized void callOnArrival(int currentFloorNumber, int destinationFloorNumber){
        //executor.schedule(callback, ArrivalSensor.getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber), TimeUnit.SECONDS);
        try {
            wait(getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber));
        }catch(InterruptedException ex)
        {
            ex.getStackTrace();
        }

    }

    public void shutdown() {
        //executor.shutdown();
    }
}
