package ElevatorSubsystem;

import utill.Config;

/**
 * ArrivalSensor is in charge of notifying an elevator when it has reached the next floor.
 * The ArrivalSensor will ask the elevator if it needs to stop for the next floor at the point where the elevator would need to start decelerating if it were to stop.
 */
public class ArrivalSensor extends Thread {
    /**
     * The Elevator the ArrivalSensor is associated with.
     */
    private final Elevator elevator;
    /**
     * The application configuration loader.
     */
    private final Config config;
    /**
     * If the ArrivalSensor is generating notifications.
     */
    private boolean run;


    /**
     * The default constructor for ArrivalSensor.
     *
     * @param config   The application configuration loader.
     * @param elevator The Elevator the ArrivalSensor is associated with.
     */
    public ArrivalSensor(Config config, Elevator elevator) {
        this.config = config;
        this.elevator = elevator;
    }

    /**
     * Get if the ArrivalSensor is not running and therefore not generating notifications.
     *
     * @return If the ArrivalSensor is not generating notifications.
     */
    public boolean isNotRunning() {
        return !run;
    }

    /**
     * The run method for generating notifications and timing the distance between floors.
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
                    Thread.sleep((long) (config.getFloatProperty("distanceBetweenFloors") / config.getFloatProperty("velocity") * 500)); // FIXME not sleeping for past floor
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
