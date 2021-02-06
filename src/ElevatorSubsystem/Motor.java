package ElevatorSubsystem;

/**
 * The Motor class represents
 * the motors that move the elevators
 * @version Feb 06, 2021
 */
public class Motor {
    private boolean directionsIsUp;
    private boolean isMoving;

    /**
     * Constructor for Motor
     */
    public Motor() {
        this.directionsIsUp = false;
        isMoving = true;
    }

    /**
     * Set the direction of the motor
     * @param directionsIsUp The direction - true for up, false for down
     */
    public void setDirectionsIsUp(boolean directionsIsUp) {
        this.directionsIsUp = directionsIsUp;
    }

    /**
     * @return true if the direction is up
     */
    public boolean directionsIsUp() {
        return directionsIsUp;
    }

    /**
     * @return true if the elevator is moving
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Set the motor to moving or not
     * @param moving The state of the motor
     */
    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
