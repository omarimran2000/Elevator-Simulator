package ElevatorSubsystem;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The Elevator Button class represents the buttons inside
 * the elevator used to request a destination floor
 *
 * @version Feb 27, 2021
 */
public class ElevatorButton {
    private final int floor;
    private boolean on;
    private final Queue<Long> onTime;
    private final Queue<Long> offTime;

    /**
     * Constructor for ElevatorButton
     *
     * @param floor The floor associated with the button
     */
    public ElevatorButton(int floor) {
        this.floor = floor;
        this.on = false;
        onTime = new LinkedList<>();
        offTime = new LinkedList<>();
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
        if (on) {
            onTime.add(System.currentTimeMillis());
        } else {
            offTime.add(System.currentTimeMillis());
        }
        this.on = on;
    }

    public Queue<Long> getOnTime() {
        return onTime;
    }

    public Queue<Long> getOffTime() {
        return offTime;
    }
}
