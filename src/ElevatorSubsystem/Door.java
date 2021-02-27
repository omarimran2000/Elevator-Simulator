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
     * Open the door
     */
    public void open() {
        System.out.println("Opening elevator door");
        this.isOpen = true;
    }

    /**
     * Close the door
     */
    public void close() {
        System.out.println("Closing elevator door");
        this.isOpen = false;
    }

}
