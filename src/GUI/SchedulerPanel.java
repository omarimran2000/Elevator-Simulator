package GUI;

import model.Destination;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Contains the list of events kept in the scheduler yet to be allocated to an elevator
 * @version April 4, 2021
 */
public class SchedulerPanel extends JPanel {
    private final List<JTextField> upDestinations;
    private final List<JTextField> downDestinations;

    /**
     * Constructor for SchedulerPanel
     * @param numFloors The number of floors in the system
     */
    public SchedulerPanel(int numFloors) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        upDestinations = new ArrayList<>();
        downDestinations = new ArrayList<>();

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Scheduler"));
        add(topPanel);

        JPanel upDestinationsPanel = new JPanel();
        upDestinationsPanel.add(new JLabel("Up Destinations"));
        upDestinationsPanel.setLayout(new BoxLayout(upDestinationsPanel, BoxLayout.PAGE_AXIS));


        JPanel upDestinationsGrid = new JPanel();
        upDestinationsGrid.setLayout(new FlowLayout());
        for (int i = 0; i < numFloors; i++) {
            JTextField text = new JTextField(String.valueOf(i));
            text.setEditable(false);
            text.setBackground(Color.white);
            upDestinationsGrid.add(text);
            upDestinations.add(text);
        }
        upDestinationsPanel.add(upDestinationsGrid);
        add(upDestinationsPanel);

        JPanel downDestinationsPanel = new JPanel();
        downDestinationsPanel.add(new JLabel("Down Destinations"));
        downDestinationsPanel.setLayout(new BoxLayout(downDestinationsPanel, BoxLayout.PAGE_AXIS));

        JPanel downDestinationsGrid = new JPanel();
        downDestinationsGrid.setLayout(new FlowLayout());
        for (int i = 0; i < numFloors; i++) {
            JTextField text = new JTextField(String.valueOf(i));
            text.setEditable(false);
            text.setBackground(Color.white);
            downDestinationsGrid.add(text);
            downDestinations.add(text);
        }
        downDestinationsPanel.add(downDestinationsGrid);
        add(downDestinationsPanel);
    }

    /**
     * Show that a destination has been added to the scheduler's list
     * @param floorNumber The destination of the event
     * @param isUp is true if the destination is upwards, false for down
     */
    public void addDestination(int floorNumber, boolean isUp) {
        (isUp ? upDestinations : downDestinations).get(floorNumber).setBackground(Color.green);
    }

    /**
     * Show that a destination (or set) has been removed from the scheduler's list
     * @param destinations The destinations
     */
    public void removeDestinations(HashSet<Destination> destinations) {
        destinations.forEach((destination) -> (destination.isUp() ? upDestinations : downDestinations).get(destination.getFloorNumber()).setBackground(Color.white));
    }
}
