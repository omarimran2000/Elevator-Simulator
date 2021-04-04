package stub;

import GUI.GuiApi;
import model.Destination;
import model.ElevatorState;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @version April 4, 2021
 */
public class GuiClient extends StubClient implements GuiApi {
    private final InetAddress inetAddress;
    private final int port;

    /**
     * The default stub client constructor.
     *
     * @param config The application configuration file loader.
     */
    public GuiClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    /**
     * Set the current floor number of the specified elevator
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The current floor number
     */
    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException {
        sendAndReceive(1, List.of(elevatorNumber, floorNumber), inetAddress, port);
    }

    /**
     * Set the specified elevator's direction
     *
     * @param elevatorNumber The specified elevator's number
     * @param direction is true for up, false for down
     */
    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException {
        sendAndReceive(2, List.of(elevatorNumber, direction), inetAddress, port);
    }

    /**
     * Set the specified elevator's doors open/closed
     *
     * @param elevatorNumber The specified elevator's number
     * @param open is true for open, false for closed
     */
    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(3, List.of(elevatorNumber, open), inetAddress, port);
    }

    /**
     * Set the specified elevator's state - (NotMoving, MovingUp, MovingDown, Stuck)
     *
     * @param elevatorNumber The specified elevator's number
     * @param state The elevator's state
     */
    @Override
    public void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException {
        sendAndReceive(4, List.of(elevatorNumber, state), inetAddress, port);
    }

    /**
     * Set the specified elevator's doors stuck
     *
     * @param elevatorNumber The specified elevator's number
     * @param doorsStuck is true if the doors are stuck, false if not
     * @param open is true if the doors are stuck open, false for closed
     */
    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException {
        sendAndReceive(5, List.of(elevatorNumber, doorsStuck, open), inetAddress, port);
    }
    /**
     * Show that a destination has been added to the specified elevator's queue
     * If the destination came from the scheduler, the floor lights up in blue
     * If the destination came from an elevatorButton pressed, the floor lights up in green
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The floor number
     * @param isButton is true if an elevatorButton was pressed, false if the scheduler added the destination
     * @param on is true if the destination is being added to the queue, false if it is being removed
     */
    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean isButton, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(6, List.of(elevatorNumber, floorNumber, isButton, on), inetAddress, port);
    }

    /**
     * Show that the floorButton on the specified floor is on/off
     * @param floorNumber The specified floor's number
     * @param direction is true if the up button is on/off, false for down
     * @param on is true if the button is on, false for off
     */
    @Override
    public void setFloorButton(int floorNumber, boolean direction, boolean on) throws IOException, ClassNotFoundException {
        sendAndReceive(7, List.of(floorNumber, direction, on), inetAddress, port);
    }

    /**
     * If an event is not able to be allocated to an elevator right away,
     * it is added to a list kept in the scheduler. Light up the specified
     * label in the schedulerPanel when a destination is added to this list
     * @param floorNumber The floor number
     * @param isUp is true of the event is up, false for down
     */
    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        sendAndReceive(8, List.of(floorNumber, isUp), inetAddress, port);
    }

    /**
     * When some destinations are allocated to an elevator from the scheduler's
     * list, remove the light from the schedulerPanel
     * @param destinations The destination(s) being removed
     */
    @Override
    public void removeSchedulerDestinations(HashSet<Destination> destinations) throws IOException, ClassNotFoundException {
        sendAndReceive(9, destinations, inetAddress, port);
    }
}
