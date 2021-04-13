package ElevatorSubsystem;

/**
 * The Elevator Button class represents the buttons inside
 * the elevator used to request a destination floor
 *
 * @version Feb 27, 2021
 */
public class ElevatorButton {
    private final int floor;
    private boolean on;

    /**
     * Constructor for ElevatorButton
     *
     * @param floor The floor associated with the button
     */
    public ElevatorButton(int floor) {
        this.floor = floor;
        this.on = false;
    }

    /**
     * Getter for floor number
     *
     * @return The floor number associated with the button
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Getter for button
     *
     * @return If its been pressed
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Setter for the mbutton
     *
     * @param on
     */
    public void setOn(boolean on) {
        this.on = on;
    }
}
