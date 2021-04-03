package stub;

import SchedulerSubsystem.SchedulerApi;
import model.Destination;
import model.Floors;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;

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
     * @param floorNumber floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Floors getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, floorNumber, inetAddress, port);
    }

    /**
     * Used by elevator to get the floors of the waiting people down
     *
     * @param floorNumber floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Floors getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException {
        return sendAndReceive(2, floorNumber, inetAddress, port);
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
        sendAndReceive(3, destination, inetAddress, port);
    }

    /**
     * Get waiting people
     *
     * @param floorNumber the destination
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public Floors getWaitingPeople(int floorNumber) throws IOException, ClassNotFoundException {
        return sendAndReceive(4, floorNumber, inetAddress, port);
    }

    @Override
    public void interrupt() throws IOException, ClassNotFoundException {
        sendAndReceive(20, inetAddress, port);
    }
}
