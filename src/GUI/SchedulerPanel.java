package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SchedulerPanel extends JPanel {
    private final List<JTextField> upDestinations;
    private final List<JTextField> downDestinations;

    public SchedulerPanel(int numFloors) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        upDestinations = new ArrayList<>();
        downDestinations = new ArrayList<>();

        int gridDimension = (int) Math.ceil(Math.sqrt(numFloors));

        JPanel topPanel = new JPanel();
        topPanel.add( new JLabel("Scheduler"));
        add(topPanel, BorderLayout.NORTH);

        JPanel upDestinationsPanel = new JPanel();
        upDestinationsPanel.add(new JLabel("Up Destinations"));
        upDestinationsPanel.setLayout(new BoxLayout(upDestinationsPanel, BoxLayout.PAGE_AXIS));

        JPanel upDestinationsGrid = new JPanel();
        upDestinationsGrid.setLayout(new GridLayout(gridDimension, gridDimension));
        for (int i = 0; i < numFloors; i++) {
            JTextField text = new JTextField(String.valueOf(i));
            text.setEditable(false);
            text.setBackground(Color.white);
            upDestinationsGrid.add(text);
            upDestinations.add(text);
        }
        upDestinationsPanel.add(upDestinationsGrid);
        add(upDestinationsPanel, BorderLayout.EAST);

        JPanel downDestinationsPanel = new JPanel();
        downDestinationsPanel.add(new JLabel("Down Destinations"));
        downDestinationsPanel.setLayout(new BoxLayout(downDestinationsPanel, BoxLayout.PAGE_AXIS));

        JPanel downDestinationsGrid = new JPanel();
        downDestinationsGrid.setLayout(new GridLayout(gridDimension, gridDimension));
        for (int i = 0; i < numFloors; i++) {
            JTextField text = new JTextField(String.valueOf(i));
            text.setEditable(false);
            text.setBackground(Color.white);
            downDestinationsGrid.add(text);
            downDestinations.add(text);
        }
        downDestinationsPanel.add(downDestinationsGrid);
        add(downDestinationsPanel, BorderLayout.WEST);
    }

    public void setDestination(int floorNumber, boolean isUp, boolean on) {
        (isUp ? upDestinations : downDestinations).get(floorNumber).setBackground(on ? Color.green : Color.white);
    }
}
