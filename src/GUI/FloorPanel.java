package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * FloorContainer contains the status of the floor buttons for each floor
 * The up/down text fields should become visible when the corresponding floor button is activated
 */
public class FloorPanel extends JPanel {
    private JLabel floorNumberLabel, upLabel, downLabel;

    public FloorPanel(int floorNumber){
        this.setVisible(true);

        floorNumberLabel = new JLabel();
        upLabel = new JLabel();
        downLabel = new JLabel();

        floorNumberLabel.setText("Floor " + floorNumber);
        floorNumberLabel.setVisible(true);

        upLabel.setText("UP");
        upLabel.setVisible(false);
        upLabel.setForeground(Color.green);

        downLabel.setText("DOWN");
        downLabel.setVisible(false);
        downLabel.setForeground(Color.red);

        this.add(floorNumberLabel);
        this.add(upLabel);
        this.add(downLabel);
    }

    public void setUp(boolean on){
        upLabel.setVisible(on);
    }

    public void setDown(boolean on){
        downLabel.setVisible(on);
    }
}
