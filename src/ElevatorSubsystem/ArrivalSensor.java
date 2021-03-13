package ElevatorSubsystem;

import utill.Config;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 27, 2021
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
     * The run method
     */
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
