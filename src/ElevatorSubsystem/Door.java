package ElevatorSubsystem;

import java.util.logging.Logger;

/**
 * The Door class represents the elevator doors
 *
 * @version Feb 27, 2021
 */
public class Door {
    private final Logger logger;
    private boolean isOpen;

    /**
     * Constructor for Door
     */
    public Door() {
        isOpen = false;
        logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Open the door
     */
    public void open() {
        logger.info("Opening elevator door");
        this.isOpen = true;
    }

    /**
     * Close the door
     */
    public void close() {
        logger.info("Closing elevator door");
        this.isOpen = false;
    }

}
