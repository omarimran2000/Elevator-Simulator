package stub;

import FloorSubsystem.FloorApi;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

/**
 * Client Socket that creates information packets (using Serializable) and sends them through the socket to
 * the other clients
 *
 * @version March 13th 2021
 */
public class FloorClient extends StubClient implements FloorApi {
    private final InetAddress inetAddress;
    private final int port;

    /**
     * /**
     * The default floor stub client constructor.
     *
     * @param config      The application configuration file loader.
     * @param inetAddress
     * @param port
     */
    public FloorClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashSet<Integer> getWaitingPeople(boolean isUp) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, isUp, inetAddress, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void interrupt() throws IOException, ClassNotFoundException {
        sendAndReceive(20, inetAddress, port);
    }
}
