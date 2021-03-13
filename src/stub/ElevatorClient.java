package stub;

import ElevatorSubsystem.ElevatorApi;
import model.Destination;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;

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
    public int distanceTheFloor(Destination destination) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, destination, inetAddress, port);
    }

    @Override
    public void addDestination(Destination destination) throws IOException, ClassNotFoundException {
        sendAndReceive(2, destination, inetAddress, port);
    }

    @Override
    public boolean canAddDestination(Destination destination) throws IOException, ClassNotFoundException {
        return sendAndReceive(3, destination, inetAddress, port);
    }
}
