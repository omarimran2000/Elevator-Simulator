package stub;

import FloorSubsystem.FloorApi;
import model.Floors;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;

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
     * Gets the floors for waiting people up
     *
     * @return floors
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Floors getWaitingPeopleUp() throws IOException, ClassNotFoundException {
        return sendAndReceive(1, inetAddress, port);
    }

    /**
     * Gets the floors for waiting people down
     *
     * @return floors
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Floors getWaitingPeopleDown() throws IOException, ClassNotFoundException {
        return sendAndReceive(2, inetAddress, port);
    }

    @Override
    public void interrupt() throws IOException, ClassNotFoundException {
        sendAndReceive(20, inetAddress, port);
    }
}
