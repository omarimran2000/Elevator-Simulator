package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * FloorContainer contains the status of the floor buttons for each floor
 * The up/down text fields should become visible when the corresponding floor button is activated
 */
public class FloorPanel extends JPanel {
    private final JLabel upLabel;
    private final JLabel downLabel;

    public FloorPanel(int floorNumber) {
        setVisible(true);

        JLabel floorNumberLabel = new JLabel();
        floorNumberLabel.setText("Floor " + floorNumber);
        floorNumberLabel.setVisible(true);
        add(floorNumberLabel);

        upLabel = new JLabel();
        upLabel.setText("UP");
        upLabel.setVisible(false);
        upLabel.setForeground(Color.green);
        add(upLabel);

        downLabel = new JLabel();
        downLabel.setText("DOWN");
        downLabel.setVisible(false);
        downLabel.setForeground(Color.red);
        add(downLabel);
    }

    /**
     * Show that the up button on this floor is turned on/off
     *
     * @param on True if the button is on, false for off
     */
    public void setUp(boolean on) {
        upLabel.setVisible(on);
    }

    /**
     * Show that the down button on this floor is turned on/off
     *
     * @param on True if the button is on, false for off
     */
    public void setDown(boolean on) {
        downLabel.setVisible(on);
    }
}
