package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SchedulerPanel extends JPanel {
    private final List<JTextField> buttons;

    public SchedulerPanel(int numFloors) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttons = new ArrayList<>();

        JPanel topPanel = new JPanel();
        topPanel.setVisible(true);
        add(topPanel, BorderLayout.NORTH);
        JLabel name = new JLabel("Scheduler");
        topPanel.add(name);
        name.setVisible(true);

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

    public void setDestination(int floorNumber, boolean on) {
        buttons.get(floorNumber).setBackground(on ? Color.green : Color.white);
    }
}
