package ElevatorSubsystem;

import java.util.Random;
import java.util.logging.Logger;
import utill.Config;

/**
 * The Door class represents the elevator doors
 *
 * @version Feb 27, 2021
 */
public class Door {
    private final Logger logger;
    private boolean isOpen;
    private final Config config;

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
        if(Math.random() * 100 < config.getIntProperty("probabilityStuck")) {
             return;
        }
        this.isOpen = true;
        logger.info("Opening elevator doors");

        }



    /**
     * Close the door
     */
    public void close() {
        if(Math.random() * 100 < config.getIntProperty("probabilityStuck")) {
            return;
        }
        this.isOpen = false;
        logger.info("Closing elevators doors");
    }


    public boolean isOpen(){
        return isOpen;
    }

}
