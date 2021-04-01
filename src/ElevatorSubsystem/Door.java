package ElevatorSubsystem;


import utill.Config;

import java.util.logging.Logger;

/**
 * The Door class represents the elevator doors
 *
 * @version Feb 27, 2021
 */
public class Door {
    private final Logger logger;
    private final Config config;
    private boolean isOpen;

    /**
     * Constructor for Door
     */
    public Door(Config config) {
        isOpen = false;
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
    }

    /**
     * Open the door
     */
    public void open() {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = true;
            logger.info("Opening elevator doors");
        }
    }


    /**
     * Close the door
     */
    public void close() {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = false;
            logger.info("Closing elevators doors");
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}
