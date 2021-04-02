package ElevatorSubsystem;


import utill.Config;

import java.util.logging.Logger;

/**
 * Door is a class meant to simulate the door of a real elevator.
 */
public class Door {
    /**
     * The Door's instance of Logger.
     */
    private final Logger logger;
    /**
     * If the Door is open.
     */
    private boolean isOpen;
    /**
     * The application configuration loader.
     */
    private final Config config;

    /**
     * The default constructor for the Door.
     *
     * @param config The application configuration loader.
     */
    public Door(Config config) {
        isOpen = false;
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
    }

    /**
     * Attempt to open the Door. This has a random chance of failing, which is configurable in the config file.
     */
    public void open() {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = true;
            logger.info("Opening elevator doors");
        }
    }


    /**
     * Attempt to close the Door. This has a random chance of failing, which is configurable in the config file.
     */
    public void close() {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = false;
            logger.info("Closing elevators doors");
        }
    }

    /**
     * Get if the Door is currently open.
     *
     * @return If the Door is open.
     */
    public boolean isOpen() {
        return isOpen;
    }
}
