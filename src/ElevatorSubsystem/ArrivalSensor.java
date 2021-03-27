package ElevatorSubsystem;

import utill.Config;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 27, 2021
 */
public class ArrivalSensor extends Thread {
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
    }

    /**
     * @return true if the elevator is running
     */
    public boolean isNotRunning() {
        return !run;
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        run = true;
        while (!Thread.interrupted()) {
            try {
                if (Math.random() * 100 < config.getFloatProperty("probabilityStuck") && (elevator.elevatorNumber == config.getIntProperty("elevatorStuck"))) {
                    Thread.sleep(100000);
                }
                Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
                if (elevator.stopForNextFloor()) {
                    Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
                    elevator.atFloor();
                } else {
                    elevator.passFloor();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
        run = false;
    }
}
