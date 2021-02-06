package FloorSubsystem;

/**
 * The Floor Button class represents the buttons on
 * each floor used to request an elevator
 * @version Feb 06, 2021
 */
public class FloorButton {
    private boolean on;

    public FloorButton() {
        this.on = false;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}