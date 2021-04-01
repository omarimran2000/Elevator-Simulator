package GUI;

import model.ElevatorState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Contains an elevator's info consisting of elevator number,
 * current floor number, State, destinations queue, doors stuck status
 */
public class ElevatorPanel extends JPanel {

    private final JPanel topPanel;
    private final JPanel statePanel;
    private final JPanel buttonsPanel;
    private final JLabel doorsOpenText;
    private final JLabel motorDirectionText;
    private final JLabel floorNumberText;
    private final JLabel stateText;
    private final JLabel doorsStuckText;
    private final ArrayList<JTextField> buttons;

    public ElevatorPanel(int numFloors, int elevatorNumber) {
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttons = new ArrayList<>();

        topPanel = new JPanel();
        topPanel.setVisible(true);
        this.add(topPanel, BorderLayout.NORTH);

        JLabel eNumber = new JLabel("Elevator " + elevatorNumber);
        topPanel.add(eNumber);
        eNumber.setVisible(true);

        statePanel = new JPanel();
        statePanel.setVisible(true);
        this.add(statePanel, BorderLayout.SOUTH);

        buttonsPanel = new JPanel();
        buttonsPanel.setVisible(true);
        this.add(buttonsPanel, BorderLayout.CENTER);

        doorsOpenText = new JLabel();
        doorsOpenText.setVisible(true);
        setDoorsOpen(true);

        motorDirectionText = new JLabel();
        motorDirectionText.setVisible(true);
        setMotorDirection(true);

        floorNumberText = new JLabel();
        floorNumberText.setVisible(true);
        floorNumberText.setText("");
        setFloor(0);

        stateText = new JLabel();
        stateText.setVisible(true);
        setStateText(ElevatorState.NotMoving);

        doorsStuckText = new JLabel();
        setDoorsStuck(false, false);

        topPanel.add(motorDirectionText);
        topPanel.add(floorNumberText);
        topPanel.add(doorsOpenText);

        statePanel.add(stateText);
        statePanel.add(doorsStuckText);

        int gridDimension = (int) Math.ceil(Math.sqrt(numFloors));
        buttonsPanel.setLayout(new GridLayout(gridDimension, gridDimension));
        int n = 0;
        while (n < numFloors) {
            JTextField text = new JTextField(String.valueOf(n));
            text.setVisible(true);
            text.setEditable(false);
            text.setBackground(Color.white);
            buttonsPanel.add(text);
            buttons.add(text);
            n++;
        }
    }

    public void setFloor(int floorNumber) {
        floorNumberText.setText("Current floor: " + floorNumber);
    }

    public void setStateText(ElevatorState state) {
        stateText.setText("Elevator state: " + state);
    }

    public void setMotorDirection(boolean direction) {
        motorDirectionText.setText("Direction: " + (direction ? "UP" : "DOWN"));
    }

    public void setDoorsOpen(boolean open) {
        doorsOpenText.setText("Doors: " + (open ? "OPEN" : "CLOSED"));
    }

    /**
     * If "stuck" is false then this label is invisible so "open" does not matter
     */
    public void setDoorsStuck(boolean stuck, boolean open) {
        doorsStuckText.setVisible(stuck);
        doorsStuckText.setText("Doors stuck " + (open ? "open" : "closed"));
    }

    /**
     * @param floorNumber
     * @param on
     */
    public void setDestination(int floorNumber, boolean on) {
        JTextField b = buttons.get(floorNumber);
        if (on) b.setBackground(Color.green);
        else b.setBackground(Color.white);
    }
}
