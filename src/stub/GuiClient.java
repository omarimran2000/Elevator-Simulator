package stub;

import GUI.GuiApi;
import model.Destination;
import model.ElevatorState;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;

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

    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException {
        sendAndReceive(1, List.of(elevatorNumber, floorNumber), inetAddress, port);
    }

    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException {
        sendAndReceive(2, List.of(elevatorNumber, direction), inetAddress, port);
    }

    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(3, List.of(elevatorNumber, open), inetAddress, port);
    }

    @Override
    public void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException {
        sendAndReceive(4, List.of(elevatorNumber, state), inetAddress, port);
    }

    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(5, List.of(elevatorNumber, doorsStuck, open), inetAddress, port);
    }

    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(6, List.of(elevatorNumber, floorNumber, on), inetAddress, port);
    }

    @Override
    public void setFloorButton(int floorButton, boolean direction, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(7, List.of(floorButton, direction, on), inetAddress, port);
    }

    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        sendAndReceive(8, List.of(floorNumber, isUp), inetAddress, port);
    }

    @Override
    public void removeSchedulerDestinations(HashSet<Destination> destinations) throws IOException, ClassNotFoundException {
        sendAndReceive(8, destinations, inetAddress, port);
    }
}
