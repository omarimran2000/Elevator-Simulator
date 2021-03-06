package FloorSubsystem;

import GUI.GuiApi;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * The Floor Button class represents the buttons on
 * each floor used to request an elevator
 *
 * @version April 4, 2021
 */
public class FloorButton {
    private final Logger logger;
    private final int floorNumber;
    private final boolean isUp;
    private final GuiApi gui;
    private boolean on;
    private final Queue<Long> onTime;
    private final Queue<Long> offTime;


    /**
     * Constructor for a floor button
     *
     * @param floorNumber the floor number
     * @param isUp        the direction of button
     * @param gui         the gui
     */
    public FloorButton(int floorNumber, boolean isUp, GuiApi gui) {
        this.floorNumber = floorNumber;
        this.isUp = isUp;
        this.gui = gui;
        logger = Logger.getLogger(this.getClass().getName());
        on = false;
        onTime = new LinkedList<>();
        offTime = new LinkedList<>();
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
    public void setOn(boolean on) throws IOException, ClassNotFoundException {
        if (this.on != on) {
            logger.info("The " + (isUp ? "up" : "down") + " button on " + floorNumber + " is " + (on ? "on" : "off"));
            this.on = on;
            gui.setFloorButton(floorNumber, isUp, on);
            if (on) {
                onTime.add(System.currentTimeMillis());
            } else {
                offTime.add(System.currentTimeMillis());
            }
        }
    }

    public Queue<Long> getOnTime() {
        return onTime;
    }

    public Queue<Long> getOffTime() {
        return offTime;
    }
}