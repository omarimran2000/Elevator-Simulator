package ElevatorSubsystem;


import GUI.GuiApi;
import utill.Config;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Door is a class meant to simulate the door of a real elevator.
 */
public class Door {
    private final int elevatorNumber;
    /**
     * The Door's instance of Logger.
     */
    private final Logger logger;
    /**
     * If the Door is open.
     */
    private final GuiApi gui;
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
    public Door(int elevatorNumber, Config config, GuiApi gui) {
        this.elevatorNumber = elevatorNumber;
        this.gui = gui;
        isOpen = false;
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
    }

    /**
     * Attempt to open the Door. This has a random chance of failing, which is configurable in the config file.
     */
    public void open() throws IOException, ClassNotFoundException {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = true;
            logger.info("Opening elevator doors");
            gui.setDoorsOpen(elevatorNumber, true);
        }
    }


    /**
     * Attempt to close the Door. This has a random chance of failing, which is configurable in the config file.
     */
    public void close() throws IOException, ClassNotFoundException {
        if (Math.random() * 100 > config.getFloatProperty("probabilityDoorStuck")) {
            isOpen = false;
            logger.info("Closing elevators doors");
            gui.setDoorsOpen(elevatorNumber, false);
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
