package ElevatorSubsystem;

public class Motor {
    private boolean directionsIsUp;
    private boolean isMoving;

    public Motor() {
        this.directionsIsUp = false;
        isMoving = true;
    }

    public void setDirectionsIsUp(boolean directionsIsUp) {
        this.directionsIsUp = directionsIsUp;
    }

    public boolean directionsIsUp() {
        return directionsIsUp;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
