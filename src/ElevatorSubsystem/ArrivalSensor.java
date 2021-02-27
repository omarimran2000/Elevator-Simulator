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


    public boolean isNotRunning() {
        return !run;
    }


    public void shutDown() {
        run = false;
    }
    public long getSecondsToTravelBetweenTwoFloors(int distance) {
        return (long) ((distance * elevator.getConfig().getFloatProperty("distanceBetweenFloors")) / elevator.getConfig().getFloatProperty("velocity"));
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
