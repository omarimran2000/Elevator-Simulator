package ElevatorSubsystem;

/**
 * The Door class represents the elevator doors
 *
 * @version Feb 06, 2021
 */
public class Door {
    private boolean isOpen;

    /**
     * Constructor for Door
     */
    public Door() {
        isOpen = false;
    }

    /**
     * Opens or closes the door
     *
     * @param isOpen The state of the door
     */
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

}
