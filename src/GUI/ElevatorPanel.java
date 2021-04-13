package GUI;

import model.ElevatorState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains an elevator's info consisting of elevator number,
 * current floor number, State, destinations queue, doors stuck status
 * Each elevatorPanel represents one elevator in the system
 *
 * @version April 4, 2021
 */
public class ElevatorPanel extends JPanel {
    private final JLabel doorsOpenText;
    private final JLabel motorDirectionText;
    private final JLabel floorNumberText;
    private final JLabel stateText;
    private final JLabel doorsStuckText;
    private final List<JTextField> buttons;

    /**
     * Constructor for elevator panel
     *
     * @param numFloors      The number of floors in the system
     * @param elevatorNumber The elevator number
     */
    public ElevatorPanel(int numFloors, int elevatorNumber) {
        setVisible(true);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttons = new ArrayList<>();

        JPanel topPanel = new JPanel();
        topPanel.setVisible(true);
        add(topPanel, BorderLayout.NORTH);

        JLabel eNumber = new JLabel("Elevator " + elevatorNumber);
        topPanel.add(eNumber);
        eNumber.setVisible(true);

        JPanel statePanel = new JPanel();
        statePanel.setVisible(true);
        add(statePanel, BorderLayout.SOUTH);

        doorsOpenText = new JLabel();
        doorsOpenText.setVisible(true);
        setDoorsOpen(false);
        topPanel.add(doorsOpenText);

        motorDirectionText = new JLabel();
        motorDirectionText.setVisible(true);
        setMotorDirection(true);
        topPanel.add(motorDirectionText);

        floorNumberText = new JLabel();
        floorNumberText.setVisible(true);
        floorNumberText.setText("");
        setFloor(0);
        topPanel.add(floorNumberText);

        stateText = new JLabel();
        stateText.setVisible(true);
        statePanel.add(stateText);
        setStateText(ElevatorState.NotMoving);

        doorsStuckText = new JLabel();
        setDoorsStuck(false, false);
        statePanel.add(doorsStuckText);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setVisible(true);
        add(buttonsPanel, BorderLayout.CENTER);

        int gridDimension = (int) Math.ceil(Math.sqrt(numFloors));
        buttonsPanel.setLayout(new GridLayout(gridDimension, gridDimension));
        for (int i = 0; i < numFloors; i++) {
            JTextField text = new JTextField(String.valueOf(i));
            text.setVisible(true);
            text.setEditable(false);
            text.setBackground(Color.white);
            buttonsPanel.add(text);
            buttons.add(text);
        }
    }

    /**
     * Set the current floor of the elevator
     *
     * @param floorNumber The current floor number
     */
    public void setFloor(int floorNumber) {
        floorNumberText.setText("Current floor: " + floorNumber);
    }

    /**
     * Set the current state of the elevator (NotMoving, MovingUp, MovingDown, Stuck)
     *
     * @param state The current state
     */
    public void setStateText(ElevatorState state) {
        stateText.setText("Elevator state: " + state.name());
    }

    /**
     * Set the current directions of the elevator
     *
     * @param direction is true for up, false for down
     */
    public void setMotorDirection(boolean direction) {
        motorDirectionText.setText("Direction: " + (direction ? "UP" : "DOWN"));
    }

    /**
     * Set the elevator's doors open or closed
     *
     * @param open is true for open, false for closed
     */
    public void setDoorsOpen(boolean open) {
        doorsOpenText.setText("Doors: " + (open ? "OPEN" : "CLOSED"));
    }

    /**
     * Set the doors stuck state of elevator - if the doors are not stuck, "open" does not apply
     *
     * @param stuck is true if the doors are stuck, false if they are not
     * @param open  is true is the doors are stuck open, false for closed
     */
    public void setDoorsStuck(boolean stuck, boolean open) {
        doorsStuckText.setVisible(stuck);
        doorsStuckText.setText("Doors stuck " + (open ? "open" : "closed"));
    }

    /**
     * Show that an elevatorButton has been pressed or that the scheduler has added a destination the elevator
     * Button presses are shown in green
     * Scheduled events are shown in blue
     * If on is false, isButton does not apply
     *
     * @param floorNumber The destination
     * @param isButton    is true if an elevator button was pressed, false if the event came from the scheduler
     * @param on          is true if the button should be on, false for off
     */
    public void setElevatorButton(int floorNumber, boolean isButton, boolean on) {
        if (!on) {
            buttons.get(floorNumber).setBackground(Color.white);
        } else if (isButton) {
            buttons.get(floorNumber).setBackground(Color.green);
        } else {
            buttons.get(floorNumber).setBackground(Color.cyan);
        }
    }
}
