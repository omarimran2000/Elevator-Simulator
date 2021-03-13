package stub;

import ElevatorSubsystem.ElevatorApi;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class ElevatorClient extends StubClient implements ElevatorApi {
    private final InetAddress inetAddress;
    private final int port;

    /**
     * The default Elevator stub client constructor.
     *
     * @param config      The application configuration file loader.
     * @param inetAddress
     * @param port
     */
    public ElevatorClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    public int distanceTheFloor(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, List.of(floorNumber, isUp), inetAddress, port);
    }

    @Override
    public void addDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        sendAndReceive(2, List.of(floorNumber, isUp), inetAddress, port);
    }
}
