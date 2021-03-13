package stub;

import FloorSubsystem.FloorApi;
import model.SendSet;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;

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
    protected FloorClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    public SendSet getWaitingPeopleUp() throws IOException, ClassNotFoundException {
        return sendAndReceive(1, inetAddress, port);
    }

    @Override
    public SendSet getWaitingPeopleDown() throws IOException, ClassNotFoundException {
        return sendAndReceive(2, inetAddress, port);
    }
}
