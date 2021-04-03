package ElevatorSubsystem;

import GUI.GuiApi;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.logging.Logger;

/**
 * The Motor class represents
 * the motors that move the elevators
 *
 * @version Feb 27, 2021
 */
public class Motor {
    private final int elevatorNumber;
    private final Logger logger;
    private final GuiApi gui;
    private boolean directionIsUp;
    private boolean isMoving;


    /**
     * Constructor for Motor
     *
     * @param elevatorNumber
     * @param gui
     */
    public Motor(int elevatorNumber, GuiApi gui) {
        this.elevatorNumber = elevatorNumber;
        this.gui = gui;
        logger = Logger.getLogger(this.getClass().getName());
        directionIsUp = true;
        isMoving = false;
    }

    /**
     * Set the direction of the motor
     *
     * @param directionIsUp The direction - true for up, false for down
     */
    public void setDirectionIsUp(boolean directionIsUp) throws IOException, ClassNotFoundException {
        logger.info("The motor direction is " + (directionIsUp ? "up" : "down"));
        gui.setMotorDirection(elevatorNumber, directionIsUp);
        this.directionIsUp = directionIsUp;
    }

    /**
     * @return true if the direction is up
     */
    public boolean directionIsUp() {
        return directionIsUp;
    }

    /**
     * @return true if the elevator is moving
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Set the motor to moving or not
     *
     * @param moving The state of the motor
     */
    public void setMoving(boolean moving) {
        isMoving = moving;
        logger.info("The motor is " + (isMoving ? "" : "not ") + "running");
    }

}
