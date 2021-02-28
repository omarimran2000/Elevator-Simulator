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


    @Override
    public void run() {
        run = true;
        while (run) {
            try {
                Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (elevator.stopForNextFloor()) {
                try {
                    Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                elevator.atFloor();

            } else {

                elevator.passFloor();
            }
        }
    }
}
