package stub;

import SchedulerSubsystem.SchedulerApi;
import model.Destination;
import model.SendSet;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class SchedulerClient extends StubClient implements SchedulerApi {
    private final InetAddress inetAddress;
    private final int port;

    /**
     * The default stub client constructor.
     *
     * @param config      The application configuration file loader.
     * @param inetAddress
     * @param port
     */
    public SchedulerClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    public SendSet getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, floorNumber, inetAddress, port);
    }

    @Override
    public SendSet getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException {
        return sendAndReceive(2, floorNumber, inetAddress, port);
    }

    @Override
    public void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException {
        sendAndReceive(3, destination, inetAddress, port);
    }
}
