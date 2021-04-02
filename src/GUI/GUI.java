package GUI;

import model.AckMessage;
import model.ElevatorState;
import stub.StubServer;
import utill.Config;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUI extends Thread implements GuiApi {
    private final Config config;
    private final List<ElevatorPanel> elevators;
    private final List<FloorPanel> floors;
    private final DatagramSocket socket;
    private final JLabel schedulerMessage;
    private final JLabel schedulerDestinations;

    public GUI(Config config) throws SocketException {
        this.config = config;
        elevators = new ArrayList<>();
        floors = new ArrayList<>();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                interrupt();
                socket.close();
                System.exit(0);
            }
        });

        Container contentPane = new Container();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setVisible(true);
        frame.add(contentPane);

        Container elevatorsContainer = new Container();
        elevatorsContainer.setLayout(new GridLayout(0, config.getIntProperty("numElevators")));
        elevatorsContainer.setVisible(true);
        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            ElevatorPanel e = new ElevatorPanel(config.getIntProperty("numFloors"), i);
            elevators.add(e);
            elevatorsContainer.add(e);
        }
        contentPane.add(elevatorsContainer, BorderLayout.CENTER);

        Container floorsContainer = new Container();
        int dim = (int) Math.ceil(Math.sqrt(config.getIntProperty("numFloors")));
        floorsContainer.setLayout(new GridLayout(dim, dim));
        floorsContainer.setVisible(true);
        for (int i = 0; i < config.getIntProperty("numFloors"); i++) {
            FloorPanel f = new FloorPanel(i);
            floors.add(f);
            floorsContainer.add(f);
        }
        //contentPane.add(floorsContainer, BorderLayout.PAGE_END);
        Container c = new Container();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
        contentPane.add(c, BorderLayout.PAGE_END);
        c.add(floorsContainer);

        Container schedulerContainer = new Container();
        schedulerContainer.setLayout(new BoxLayout(schedulerContainer, BoxLayout.PAGE_AXIS));
        c.add(schedulerContainer);

        schedulerMessage = new JLabel();
        schedulerDestinations = new JLabel();
        schedulerContainer.add(schedulerMessage);
        schedulerContainer.add(schedulerDestinations);
        setSchedulerMessage("");
        setSchedulerDestinations("");

        //scheduler = new JLabel();
        //contentPane.add(scheduler, BorderLayout.PAGE_END);
        //scheduler.setVisible(true);

        frame.pack();
        frame.setSize(1750, 700);
        frame.setVisible(true);

        socket = new DatagramSocket(config.getIntProperty("GUIPort"));
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        new GUI(config).start();
    }

    /**
     * Sets the elevator's current floor number
     *
     * @param elevatorNumber
     * @param floorNumber
     */
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) {
        elevators.get(elevatorNumber).setFloor(floorNumber);
    }

    /**
     * Sets the elevator's current direction of travel
     *
     * @param elevatorNumber
     * @param direction
     */
    public void setMotorDirection(int elevatorNumber, boolean direction) {
        elevators.get(elevatorNumber).setMotorDirection(direction);
    }

    /**
     * Opens or closes the elevator doors
     *
     * @param elevatorNumber
     * @param open
     */
    public void setDoorsOpen(int elevatorNumber, boolean open) {
        elevators.get(elevatorNumber).setDoorsOpen(open);
    }

    /**
     * Sets the elevator;'s state: idle, moving, or stuck
     *
     * @param elevatorNumber
     * @param state
     */
    public void setState(int elevatorNumber, ElevatorState state) {
        elevators.get(elevatorNumber).setStateText(state);
    }

    /**
     * Sets the elevator's doors stuck open or closed
     *
     * @param elevatorNumber
     * @param doorsStuck
     */
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) {
        elevators.get(elevatorNumber).setDoorsStuck(doorsStuck, open);
    }

    /**
     * Turns the button in the elevator corresponding to the floorNumber on or off
     *
     * @param elevatorNumber
     * @param floorNumber
     * @param on
     */
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on) {
        elevators.get(elevatorNumber).setDestination(floorNumber, on);
    }

    /**
     * Sets the up or down floor button on or off
     *
     * @param floorNumber
     * @param direction
     * @param on
     */
    public void setFloorButton(int floorNumber, boolean direction, boolean on) {
        if (direction) {
            floors.get(floorNumber).setUp(on);
        } else floors.get(floorNumber).setDown(on);
    }

    public void setSchedulerMessage(String message) {
        schedulerMessage.setText("Scheduler info: " + message);
    }

    public void setSchedulerDestinations(String destinations) {
        schedulerDestinations.setText("Destinations to be scheduled: " + destinations);
    }

    /**
     * Run method for the GUI
     */
    @Override
    public void run() {
        try {
            StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                    1, input -> {
                        setCurrentFloorNumber((int) input.get(0), (int) input.get(1));
                        return new AckMessage();
                    },
                    2, input -> {
                        setMotorDirection((int) input.get(0), (boolean) input.get(1));
                        return new AckMessage();
                    },
                    3, input -> {
                        setDoorsOpen((int) input.get(0), (boolean) input.get(1));
                        return new AckMessage();
                    },
                    4, input -> {
                        setState((int) input.get(0), (ElevatorState) input.get(1));
                        return new AckMessage();
                    },
                    5, input -> {
                        setDoorsStuck((int) input.get(0), (boolean) input.get(1), (boolean) input.get(2));
                        return new AckMessage();
                    },
                    6, input -> {
                        setElevatorButton((int) input.get(0), (int) input.get(1), (boolean) input.get(2));
                        return new AckMessage();
                    },
                    7, input -> {
                        setFloorButton((int) input.get(0), (boolean) input.get(1), (boolean) input.get(2));
                        return new AckMessage();
                    },
                    8, input -> {
                        setSchedulerMessage((String) input.get(0));
                        return new AckMessage();
                    },
                    9, input -> {
                        setSchedulerMessage((String) input.get(0));
                        return new AckMessage();
                    }

            ));
        } catch (SocketException e) {
            if (!Thread.interrupted()) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}