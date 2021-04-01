package GUI;

import model.AckMessage;
import model.ElevatorState;
import stub.StubServer;
import utill.Config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;

public class GUI implements GuiApi, Runnable {
    private final Config config;
    private final JFrame frame;
    private final Container contentPane;
    private final Container elevatorsContainer;
    private final Container floorsContainer;
    private final ArrayList<ElevatorPanel> elevators;
    private final ArrayList<FloorPanel> floors;
    private final int numElevators;
    private final int numFloors;
    private final DatagramSocket socket;

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        new Thread(new GUI(config)).start();
    }

    public GUI(Config config) throws SocketException {
        this.config = config;
        elevators = new ArrayList<>();
        floors = new ArrayList<>();
        numElevators = config.getIntProperty("numElevators");
        numFloors = config.getIntProperty("numFloors");

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        contentPane = new Container();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setVisible(true);

        elevatorsContainer = new Container();
        elevatorsContainer.setLayout(new GridLayout(0, numElevators));
        elevatorsContainer.setVisible(true);
        floorsContainer = new Container();

        frame.add(contentPane);
        contentPane.add(elevatorsContainer, BorderLayout.CENTER);
        contentPane.add(floorsContainer, BorderLayout.PAGE_END);

        for (int i = 0; i < numElevators; i++) {
            ElevatorPanel e = new ElevatorPanel(numFloors, i);
            elevators.add(e);
            elevatorsContainer.add(e);
        }

        int dim = (int) Math.ceil(Math.sqrt(numFloors));
        floorsContainer.setLayout(new GridLayout(dim, dim));
        floorsContainer.setVisible(true);
        for (int i = 0; i < numFloors; i++) {
            FloorPanel f = new FloorPanel(i);
            floors.add(f);
            floorsContainer.add(f);
        }
        frame.pack();
        frame.setSize(1250, 700);
        socket = new DatagramSocket(config.getIntProperty("GUIPort"));
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

    /**
     * Run method for the GUI
     */
    @Override
    public void run() {
        try {
            StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                    1, input ->
                    {
                        setCurrentFloorNumber((int) input.get(0), (int) input.get(1));
                        return new AckMessage();
                    },
                    2, input ->
                    {
                        setMotorDirection((int) input.get(0), (boolean) input.get(1));
                        return new AckMessage();
                    },
                    3, input ->
                    {
                        setDoorsOpen((int) input.get(0), (boolean) input.get(1));
                        return new AckMessage();
                    },
                    4, input ->
                    {
                        setState((int) input.get(0), (ElevatorState) input.get(1));
                        return new AckMessage();
                    },
                    5, input ->
                    {
                        setDoorsStuck((int) input.get(0), (boolean) input.get(1), (boolean) input.get(2));
                        return new AckMessage();
                    },
                    6, input ->
                    {
                        setElevatorButton((int) input.get(0), (int) input.get(1), (boolean) input.get(2));
                        return new AckMessage();
                    },
                    7, input ->
                    {
                        setFloorButton((int) input.get(0), (boolean) input.get(1), (boolean) input.get(2));
                        return new AckMessage();
                    }

            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
