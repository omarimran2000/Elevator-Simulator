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
 *
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
     * {@inheritDoc}
     */
    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException {
        sendAndReceive(1, List.of(elevatorNumber, floorNumber), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException {
        sendAndReceive(2, List.of(elevatorNumber, direction), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(3, List.of(elevatorNumber, open), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException {
        sendAndReceive(4, List.of(elevatorNumber, state), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(5, List.of(elevatorNumber, doorsStuck, open), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean isButton, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(6, List.of(elevatorNumber, floorNumber, isButton, on), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloorButton(int floorNumber, boolean direction, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(7, List.of(floorNumber, direction, on), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        sendAndReceive(8, List.of(floorNumber, isUp), inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSchedulerDestinations(HashSet<Destination> destinations) throws IOException, ClassNotFoundException {
        sendAndReceive(9, destinations, inetAddress, port);
    }
}
