package ElevatorSubsystem;

import utill.Config;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Arrival Sensor is a sensor in the Elevator
 * to notify the schedule that it has arrived at a floor
 *
 * @version Feb 27, 2021
 */
public class ArrivalSensor extends Thread {
    private final Elevator elevator;
    private final Config config;
    private final AtomicBoolean stuck;
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
        stuck = new AtomicBoolean(false);
    }

    /**
     * @return true if the elevator is running
     */
    public boolean isNotRunning() {
        return !run;
    }

    public boolean isStuck() {
        return stuck.get();
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        run = true;
        while (!Thread.interrupted() && !stuck.get()) {
            try {
                if (Math.random() * 100 < config.getFloatProperty("probabilityStuck") && (elevator.elevatorNumber == config.getIntProperty("elevatorStuck"))) {
                    stuck.set(true);
                    return;
                }
                Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
                if (elevator.stopForNextFloor()) {
                    Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
                    elevator.atFloor();
                } else {
                    Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500));
                    elevator.passFloor();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
        run = false;
    }
}
