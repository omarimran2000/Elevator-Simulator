package stub;

import GUI.GuiApi;
import model.Destination;
import model.ElevatorState;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;

/**
 * The client for the GUI
 * @version April 4th 2021
 */
public class GuiClient extends StubClient implements GuiApi {
    private final InetAddress inetAddress;
    private final int port;

    /**
     * The default stub client constructor.
     *
     * @param config The application configuration file loader.
     */
    public GuiClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    /**
     * Setting the current floor of the elevator panel
     * @param elevatorNumber the elevator number
     * @param floorNumber the new floor number
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException {
        sendAndReceive(1, List.of(elevatorNumber, floorNumber), inetAddress, port);
    }

    /**
     * Setting the motor direction for the elevator panel
     * @param elevatorNumber the elevator number
     * @param direction true if up
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException {
        sendAndReceive(2, List.of(elevatorNumber, direction), inetAddress, port);
    }

    /**
     * Setting the doors to open in the elevator panel
     * @param elevatorNumber the elevator number
     * @param open true if open
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(3, List.of(elevatorNumber, open), inetAddress, port);
    }

    /**
     * Setting the state of the elevator in the elevator panel
     * @param elevatorNumber the elevator number
     * @param state the new state
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException {
        sendAndReceive(4, List.of(elevatorNumber, state), inetAddress, port);
    }

    /**
     * Setting the doors to stuck in the elevator panel
     * @param elevatorNumber the elevator number
     * @param doorsStuck true if stuck
     * @param open true if stuck open
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(5, List.of(elevatorNumber, doorsStuck, open), inetAddress, port);
    }

    /**
     * Setting the elevator button in the panel
     * @param elevatorNumber number of elevator
     * @param floorNumber the floor number
     * @param isButton true if elevator button, else it is destination
     * @param on true if on
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean isButton, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(6, List.of(elevatorNumber, floorNumber, isButton, on), inetAddress, port);
    }

    /**
     * Setting the floor button in the floor panel
     * @param floorButton the number of the floor
     * @param direction true if up
     * @param on true if on
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void setFloorButton(int floorButton, boolean direction, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(7, List.of(floorButton, direction, on), inetAddress, port);
    }

    /**
     * Adding the destination in the scheduler panel
     * @param floorNumber the floor number
     * @param isUp true if up
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        sendAndReceive(8, List.of(floorNumber, isUp), inetAddress, port);
    }

    /**
     * Removing the destinations from the scheduler panel
     * @param destinations the destinations to be removed
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void removeSchedulerDestinations(HashSet<Destination> destinations) throws IOException, ClassNotFoundException {
        sendAndReceive(9, destinations, inetAddress, port);
    }
}
