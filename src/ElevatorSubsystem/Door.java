package ElevatorSubsystem;


import GUI.GuiApi;
import utill.Config;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The Door class represents the elevator doors
 *
 * @version Feb 27, 2021
 */
public class Door {
    private final int elevatorNumber;
    private final Logger logger;
    private final Config config;
    private final GuiApi gui;
    private boolean isOpen;

    /**
     * Constructor for Door
     */
    public Door(int elevatorNumber, Config config, GuiApi gui) {
        this.elevatorNumber = elevatorNumber;
        this.gui = gui;
        isOpen = false;
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
    }

    /**
     * Open the door
     */
    public void open() throws IOException, ClassNotFoundException {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = true;
            logger.info("Opening elevator doors");
            gui.setDoorsOpen(elevatorNumber, true);
        }
    }


    /**
     * Close the door
     */
    public void close() throws IOException, ClassNotFoundException {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = false;
            logger.info("Closing elevators doors");
            gui.setDoorsOpen(elevatorNumber, false);
        }
    }

    /**
     * Checks if the door is open
     * @return isOpen is the status of the door
     */
    public boolean isOpen() {
        return isOpen;
    }
}
