package ElevatorSubsystem;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 27, 2021
 */
public class ArrivalSensor implements Runnable {
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
     * @return true if the elevator is running
     */
    public boolean isNotRunning() {
        return !run;
    }


    /**
     * Shuts down the elevator
     */
    public void shutDown() {
        run = false;
    }

    /**
     * Get the time in seconds to travel between two floors given the distance in meters between them
     * @param distance The distance to travel
     * @return
     */
    public long getSecondsToTravelBetweenTwoFloors(int distance) {
        return (long) ((distance * elevator.getConfig().getFloatProperty("distanceBetweenFloors")) / elevator.getConfig().getFloatProperty("velocity"));
    }

    /**
     * The run method
     */
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
            if(elevator.motor.directionIsUp()){
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
