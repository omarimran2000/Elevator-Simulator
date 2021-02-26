package ElevatorSubsystem;

import util.Config;

import static java.lang.Math.abs;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 06, 2021
 */
public class ArrivalSensor {
    private final Config config;

    /**
     * Constructor for ArrivalSensor
     * Instantiates a ScheduledExecutorService
     *
     * @param config The application configuration file loader.
     */
    public ArrivalSensor(Config config) {
        this.config = config;
    }

    /**
     * Method to determine travel time between two floors
     *
     * @param currentFloorNumber     The current floor
     * @param destinationFloorNumber The destination floor
     * @return The time in seconds to travel between the two floors
     */
    public long getSecondsToTravelBetweenTwoFloors(int currentFloorNumber, int destinationFloorNumber) {
        return (long) ((abs(destinationFloorNumber - currentFloorNumber) * config.getFloatProperty("distanceBetweenFloors")) / config.getFloatProperty("velocity"));
    }


    /**
     * Method to move the elevator based on the time
     *
     * @param currentFloorNumber     The number of the current floor
     * @param destinationFloorNumber The number of the destination floor
     */

    public synchronized void callOnArrival(int currentFloorNumber, int destinationFloorNumber) {

        try {
            wait(getSecondsToTravelBetweenTwoFloors(currentFloorNumber, destinationFloorNumber) * 1000);
        } catch (InterruptedException ex) {
            ex.getStackTrace();
        }


    }
}
