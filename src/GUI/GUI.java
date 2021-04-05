package GUI;

import ElevatorSubsystem.ElevatorApi;
import FloorSubsystem.FloorApi;
import SchedulerSubsystem.SchedulerApi;
import model.AckMessage;
import model.Destination;
import model.ElevatorState;
import stub.ElevatorClient;
import stub.FloorClient;
import stub.SchedulerClient;
import stub.StubServer;
import utill.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The GUI representation for the system comprised of one panel for each
 * of the ElevatorSubsystem, the FloorSubsystem, and the SchedulerSubsystem
 *
 * @version April 4, 2021
 */
public class GUI extends Thread implements GuiApi {
    private final Config config;
    private final List<ElevatorPanel> elevatorsPanel;
    private final List<FloorPanel> floorsPanel;
    private final SchedulerPanel schedulerPanel;
    private final DatagramSocket socket;

    /**
     * Constructor for GUI
     * @param config The
     * @param scheduler The scheduler
     * @param floors The list of floors in the system
     * @param elevators The list of elevators in the system
     * @throws SocketException
     */
    public GUI(Config config, SchedulerApi scheduler, List<FloorApi> floors, List<ElevatorApi> elevators) throws SocketException {
        this.config = config;
        elevatorsPanel = new ArrayList<>();
        floorsPanel = new ArrayList<>();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    for (ElevatorApi elevator : elevators) {
                        elevator.interrupt();
                    }
                    for (FloorApi floor : floors) {
                        floor.interrupt();
                    }
                    scheduler.interrupt();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
            elevatorsPanel.add(e);
            elevatorsContainer.add(e);
        }
        contentPane.add(elevatorsContainer, BorderLayout.CENTER);

        Container floorsContainer = new Container();
        int dim = (int) Math.ceil(Math.sqrt(config.getIntProperty("numFloors")));
        floorsContainer.setLayout(new GridLayout(dim, dim));
        floorsContainer.setVisible(true);
        for (int i = 0; i < config.getIntProperty("numFloors"); i++) {
            FloorPanel f = new FloorPanel(i);
            floorsPanel.add(f);
            floorsContainer.add(f);
        }
        contentPane.add(floorsContainer, BorderLayout.PAGE_END);

        schedulerPanel = new SchedulerPanel(config.getIntProperty("numFloors"));
        contentPane.add(schedulerPanel);
        schedulerPanel.setSize(1250, 100);

        frame.pack();
        frame.setSize(1750, 700);
        frame.setVisible(true);

        socket = new DatagramSocket(config.getIntProperty("GUIPort"));
    }

    public static void main(String[] args) throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        Config config = new Config();
        SchedulerApi schedulerApi = new SchedulerClient(config, localhost, config.getIntProperty("schedulerPort"));
        List<FloorApi> floorApis = new ArrayList<>();
        for (int i = 0; i < config.getIntProperty("numFloors"); i++) {
            floorApis.add(new FloorClient(config, localhost, config.getIntProperty("floorPort") + i));
        }
        List<ElevatorApi> elevatorApis = new ArrayList<>();
        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            elevatorApis.add(new ElevatorClient(config, localhost, config.getIntProperty("elevatorPort") + i));
        }
        new GUI(config, schedulerApi, floorApis, elevatorApis).start();
    }

    /**
     * Set the current floor number of the specified elevator
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The current floor number
     */
    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) {
        elevatorsPanel.get(elevatorNumber).setFloor(floorNumber);
    }

    /**
     * Set the specified elevator's direction
     *
     * @param elevatorNumber The specified elevator's number
     * @param direction is true for up, false for down
     */
    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) {
        elevatorsPanel.get(elevatorNumber).setMotorDirection(direction);
    }

    /**
     * Set the specified elevator's doors open/closed
     *
     * @param elevatorNumber The specified elevator's number
     * @param open is true for open, false for closed
     */
    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) {
        elevatorsPanel.get(elevatorNumber).setDoorsOpen(open);
    }

    /**
     * Set the specified elevator's state - (NotMoving, MovingUp, MovingDown, Stuck)
     *
     * @param elevatorNumber The specified elevator's number
     * @param state The elevator's state
     */
    @Override
    public void setState(int elevatorNumber, ElevatorState state) {
        elevatorsPanel.get(elevatorNumber).setStateText(state);
    }

    /**
     * Set the specified elevator's doors stuck
     *
     * @param elevatorNumber The specified elevator's number
     * @param doorsStuck is true if the doors are stuck, false if not
     * @param open is true if the doors are stuck open, false for closed
     */
    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) {
        elevatorsPanel.get(elevatorNumber).setDoorsStuck(doorsStuck, open);
    }

    /**
     * Show that a destination has been added to the specified elevator's queue
     * If the destination came from the scheduler, the floor lights up in blue
     * If the destination came from an elevatorButton pressed, the floor lights up in green
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The floor number
     * @param isButton is true if an elevatorButton was pressed, false if the scheduler added the destination
     * @param on is true if the destination is being added to the queue, false if it is being removed
     */
    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean isButton, boolean on) {
        elevatorsPanel.get(elevatorNumber).setElevatorButton(floorNumber, isButton, on);
    }

    /**
     * Show that the floorButton on the specified floor is on/off
     * @param floorNumber The specified floor's number
     * @param direction is true if the up button is on/off, false for down
     * @param on is true if the button is on, false for off
     */
    @Override
    public void setFloorButton(int floorNumber, boolean direction, boolean on) {
        if (direction) {
            floorsPanel.get(floorNumber).setUp(on);
        } else floorsPanel.get(floorNumber).setDown(on);
    }

    /**
     * If an event is not able to be allocated to an elevator right away,
     * it is added to a list kept in the scheduler. Light up the specified
     * label in the schedulerPanel when a destination is added to this list
     * @param floorNumber The floor number
     * @param isUp is true of the event is up, false for down
     */
    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) {
        schedulerPanel.addDestination(floorNumber, isUp);
    }

    /**
     * When some destinations are allocated to an elevator from the scheduler's
     * list, remove the light from the schedulerPanel
     * @param destinations The destination(s) being removed
     */
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
                        setElevatorButton((int) input.get(0), (int) input.get(1), (boolean) input.get(2), (boolean) input.get(3));
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
