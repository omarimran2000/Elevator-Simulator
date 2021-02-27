package ElevatorSubsystem;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 06, 2021
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

    @Override
    public void run() {
        run = true;
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //FIXME add physics
            if (elevator.stopForNextFloor()) {
                elevator.atFloor();
            } else {
                elevator.passFloor();
            }
        }
    }
}
