package GUI;

import model.AckMessage;
import model.Destination;
import model.ElevatorState;
import stub.StubServer;
import utill.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GUI extends Thread implements GuiApi {
    private final Config config;
    private final List<ElevatorPanel> elevators;
    private final List<FloorPanel> floors;
    private final SchedulerPanel schedulerPanel;
    private final DatagramSocket socket;

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
        contentPane.add(floorsContainer, BorderLayout.PAGE_END);

        schedulerPanel = new SchedulerPanel(config.getIntProperty("numFloors"));
        contentPane.add(schedulerPanel);
        schedulerPanel.setSize(1250, 100);

        frame.pack();
        frame.setSize(1250, 700);
        frame.setVisible(true);

        socket = new DatagramSocket(config.getIntProperty("GUIPort"));
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        new GUI(config).start();
    }


    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) {
        elevators.get(elevatorNumber).setFloor(floorNumber);
    }

    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) {
        elevators.get(elevatorNumber).setMotorDirection(direction);
    }

    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) {
        elevators.get(elevatorNumber).setDoorsOpen(open);
    }

    @Override
    public void setState(int elevatorNumber, ElevatorState state) {
        elevators.get(elevatorNumber).setStateText(state);
    }

    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) {
        elevators.get(elevatorNumber).setDoorsStuck(doorsStuck, open);
    }

    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on) {
        elevators.get(elevatorNumber).setDestination(floorNumber, on);
    }

    @Override
    public void setFloorButton(int floorNumber, boolean direction, boolean on) {
        if (direction) {
            floors.get(floorNumber).setUp(on);
        } else floors.get(floorNumber).setDown(on);
    }
    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) {
        schedulerPanel.addDestination(floorNumber,  isUp);
    }

    @Override
    public void removeSchedulerDestinations(HashSet<Destination> destinations) {
        schedulerPanel.removeDestinations(destinations);
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
                        addSchedulerDestination((int) input.get(0), (boolean) input.get(1));
                        return new AckMessage();
                    },
                    9, input -> {
                        removeSchedulerDestinations((HashSet<Destination>) input.get(0));
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
