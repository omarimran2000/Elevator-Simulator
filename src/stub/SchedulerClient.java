package stub;

import SchedulerSubsystem.SchedulerApi;
import model.Destination;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

/**
 * The client used to send UDP messages to the Scheduler
 *
 * @version March 13th 2021
 */
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

    /**
     * Used by elevator to get the floors of the waiting people up
     *
     * @param destination floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public HashSet<Integer> getWaitingPeople(Destination destination) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, destination, inetAddress, port);
    }

    /**
     * Used by the floor to handle floor button
     *
     * @param destination the destination
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException {
        sendAndReceive(2, destination, inetAddress, port);
    }

    /**
     * Get waiting people
     *
     * @param floorNumber the destination
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public HashSet<Destination> getUnscheduledPeople(int floorNumber) throws IOException, ClassNotFoundException {
        return sendAndReceive(3, floorNumber, inetAddress, port);
    }

    @Override
    public void interrupt() throws IOException, ClassNotFoundException {
        sendAndReceive(20, inetAddress, port);
    }
}
