package FloorSubsystem;

/**
 * The Direction Lamp class represents the lamps on the floors
 * that tell the users which direction the elevator is travelling in
 *
 * @version Feb 27, 2021
 */
public class DirectionLamp {
    private boolean isLit;
    private boolean direction;

    /**
     * Constructor for DirectionLamp
     */
    public DirectionLamp() {
        isLit = false;
        direction = false;
    }

    /**
     * Getter for direction
     *
     * @return true if direction is up
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * Set the direction of the lamp - true for up, false for down
     *
     * @param direction The direction of the lamp
     */
    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    /**
     * Getter for the state of the lamp
     *
     * @return true if the lamp is lit
     */
    public boolean isLit() {
        return isLit;
    }

    /**
     * Turn the lamp on or off
     *
     * @param isLit The state of the lamp
     */
    public void setLit(boolean isLit) {
        this.isLit = isLit;
    }
}
