package ElevatorSubsystem;

import FloorSubsystem.Floor;

import static java.lang.Math.abs;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 * @version Feb 06, 2021
 */
public class ArrivalSensor {

    private static final long DISTANCE_BETWEEN_FLOORS = (long) 3.5;
    private static final long VELOCITY = (long) 1.27;

    /**
     * Constructor for ArrivalSensor
     * Instantiates a ScheduledExecutorService
     */
    public ArrivalSensor() {
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
     * Method to move the elevator based on the time
     *
     * @param currentFloorNumber The number of the current floor
     * @param destinationFloorNumber The number of the destination floor
     */

    public synchronized void callOnArrival(int currentFloorNumber, int destinationFloorNumber){

        try {
            wait(getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber)*1000);
        }catch(InterruptedException ex)
        {
            ex.getStackTrace();
        }


    }
    public boolean isRequest(Floor floor, boolean directionUp){
        if(directionUp)
        {
            try {
                return floor.getTop().isOn();
            }catch(NullPointerException np)
            {
                return false;
            }
        }
        else
        {
            try {
                return floor.getBottom().isOn();
            }catch(NullPointerException np)
            {
                return false;
            }
        }
    }


}
