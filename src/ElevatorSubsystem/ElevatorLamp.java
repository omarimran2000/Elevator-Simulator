package ElevatorSubsystem;

import java.util.logging.Logger;

/**
 * The Elevator Lamp represents the
 * lamps that light up when an Elevator Button is pressed
 *
 * @version Feb 27, 2021
 */
public class ElevatorLamp {
    private final int elevatorNumber;
    private final int floorNumber;
    private final Logger logger;
    private boolean isLit;

    /**
     * Constructor for ElevatorLamp
     *
     * @param elevatorNumber
     * @param floorNumber
     */
    public ElevatorLamp(int elevatorNumber, int floorNumber) {
        this.elevatorNumber = elevatorNumber;
        this.floorNumber = floorNumber;
        logger = Logger.getLogger(this.getClass().getName());
        isLit = false;
    }

    /**
     * Set the state of the lamp
     *
     * @param lit The state of the lamp
     */
    public void setLamp(boolean lit) {
        isLit = lit;
        logger.info("Elevator " + elevatorNumber + " floor " + floorNumber + " lamp is now " + (isLit ? "on" : "off"));
    }

    /**
     * Getter for the state of the lamp
     *
     * @return true is the lamp is on
     */
    public boolean isLit() {
        return isLit;
    }

}
