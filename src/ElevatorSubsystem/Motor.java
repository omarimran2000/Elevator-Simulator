package ElevatorSubsystem;

/**
 * The Motor class represents
 * the motors that move the elevators
 *
 * @version Feb 06, 2021
 */
public class Motor {
    private boolean directionIsUp;
    private boolean isMoving;

    /**
     * Constructor for Motor
     */
    public Motor() {
        this.directionIsUp = true;
        isMoving = true;
    }

    /**
     * Set the direction of the motor
     *
     * @param directionsIsUp The direction - true for up, false for down
     */
    public void setDirectionIsUp(boolean directionsIsUp) {
        this.directionIsUp = directionsIsUp;
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
    }
}
