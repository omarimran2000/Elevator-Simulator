package ElevatorSubsystem;

import util.Config;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 06, 2021
 */
public class ArrivalSensor implements Runnable {
    private final Elevator elevator;
    private final Config config;
    private boolean run;


    /**
     * Constructor for ArrivalSensor
     * Instantiates a ScheduledExecutorService
     *
     * @param config
     * @param elevator
     */
    public ArrivalSensor(Config config, Elevator elevator) {
        this.config = config;
        this.elevator = elevator;
        run = false;
    }


    public boolean isNotRunning() {
        return !run;
    }


    public void shutDown() {
        run = false;
    }

    public long getSecondsToTravelBetweenTwoFloors(int distance) {
        return (long) ((distance * config.getFloatProperty("distanceBetweenFloors")) / config.getFloatProperty("velocity"));
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

            int nextFloor;
            if (elevator.motor.directionIsUp()) {
                nextFloor = elevator.currentFloorNumber + 1;

            } else {
                nextFloor = elevator.currentFloorNumber - 1;
            }
            int distance = elevator.distanceTheFloor(nextFloor, elevator.motor.directionIsUp());
            long seconds = getSecondsToTravelBetweenTwoFloors(distance);
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (elevator.stopForNextFloor()) {

                elevator.atFloor();

            } else {

                elevator.passFloor();
            }
        }
    }
}
