package ElevatorSubsystem;

import java.util.Random;
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
        logger.info("Trying to open elevator door");
        Random r = new Random();
        int rand = r.nextInt(4);
        if(rand == 3){
            this.isOpen = true;
            logger.info("Opening elevator doors");
        }

    }

    /**
     * Close the door
     */
    public void close() {
        logger.info("Trying to close elevator door");
        Random r = new Random();
        int rand = r.nextInt(4);
        if(rand == 3){
            this.isOpen = false;
            logger.info("Closing elevators doors");
        }

    }

    public boolean isOpen(){
        return isOpen;
    }

}
