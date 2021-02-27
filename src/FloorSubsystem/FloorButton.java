package FloorSubsystem;

/**
 * The Floor Button class represents the buttons on
 * each floor used to request an elevator
 *
 * @version Feb 27, 2021
 */
public class FloorButton {
    private boolean on;

    /**
     * Constructor for a floor button
     */
    public FloorButton() {
        this.on = false;
    }

    /**
     * Check if button is on
     *
     * @return if its on
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Turn the button on
     *
     * @param on to turn it on
     */
    public void setOn(boolean on) {
        this.on = on;
    }
}