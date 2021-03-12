package stub;

import FloorSubsystem.FloorApi;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

public class Floor extends StubClient implements FloorApi {
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
    protected Floor(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    public Set<Integer> getWaitingPeopleUp() throws IOException, ClassNotFoundException {
        return sendAndReceive(1, inetAddress, port);
    }

    @Override
    public Set<Integer> getWaitingPeopleDown() throws IOException, ClassNotFoundException {
        return sendAndReceive(2, inetAddress, port);
    }
}
