package FloorSubsystem;

import java.util.logging.Logger;

/**
 * The Floor Button class represents the buttons on
 * each floor used to request an elevator
 *
 * @version Feb 27, 2021
 */
public class FloorButton {
    private final Logger logger;
    private final int floorNumber;
    private final boolean isUp;
    private boolean on;


    /**
     * Constructor for a floor button
     *
     * @param floorNumber
     * @param isUp
     */
    public FloorButton(int floorNumber, boolean isUp) {
        this.floorNumber = floorNumber;
        this.isUp = isUp;
        logger = Logger.getLogger(this.getClass().getName());
        on = false;
    }

    /**
     * Check if button is on
     *
     * @return if its on
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Turn the button on
     *
     * @param on to turn it on
     */
    public void setOn(boolean on) {
        if (this.on != on) {
            logger.info("The " + (isUp ? "up" : "down") + " button on " + floorNumber + " is " + (on ? "on" : "off"));
            this.on = on;
        }
    }
}