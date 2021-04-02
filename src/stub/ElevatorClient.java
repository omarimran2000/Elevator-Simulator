package stub;

import ElevatorSubsystem.ElevatorApi;
import model.Destination;
import utill.Config;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * The Client Socket that creates information packets (using serializable) to be sent through the socket
 * to the other clients
 *
 * @version March 13th 2021
 */
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

    /**
     * The distance to the floor
     *
     * @param destination floor
     * @return distance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public int distanceTheFloor(Destination destination) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, destination, inetAddress, port);
    }

    /**
     * Adding the destination to the floor
     *
     * @param destination
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void addDestination(Destination destination) throws IOException, ClassNotFoundException {
        sendAndReceive(2, destination, inetAddress, port);
    }

    /**
     * Returns if destination can be added
     *
     * @param destination floor
     * @return if can be added
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public boolean canAddDestination(Destination destination) throws IOException, ClassNotFoundException {
        return sendAndReceive(3, destination, inetAddress, port);
    }


    public boolean interruptThread() throws IOException, ClassNotFoundException {
        return sendAndReceive(4, (Serializable) null, inetAddress, port);
    }

}
