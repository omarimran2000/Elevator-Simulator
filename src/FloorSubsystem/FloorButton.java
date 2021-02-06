package FloorSubsystem;

public class FloorButton {
    private boolean on;

    /**
     * Constructor for a floor button
     */
    public FloorButton() {
        this.on = false;
    }

    /**
     * Turn the button on
     * @param on to turn it on
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * Check if button is on
     * @return if its on
     */
    public boolean isOn() {
        return on;
    }
}