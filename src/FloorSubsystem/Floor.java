package FloorSubsystem;

import GUI.GuiApi;
import SchedulerSubsystem.SchedulerApi;
import model.AckMessage;
import model.Destination;
import model.Event;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;
import java.util.logging.Logger;

/**
 * The Floor class represents a single floor in the building
 *
 * @version April 4, 2021
 */
public abstract class Floor extends Thread implements FloorApi {
    private final Logger logger;
    private final SchedulerApi scheduler;
    private final PriorityQueue<Event> schedule;
    private final Map<Boolean, Set<Integer>> waitingPeople;
    private final Thread receiveThread;

    private final int floorNumber;
    private final DatagramSocket socket;

    /**
     * Constructor
     *
     * @param config      The config file
     * @param floorNumber the floor's number
     * @param scheduler   the scheduler
     * @param schedule    A list of events
     */
    public Floor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) throws SocketException {
        logger = Logger.getLogger(this.getClass().getName());
        this.floorNumber = floorNumber;
        this.scheduler = scheduler;
        this.schedule = new PriorityQueue<>(schedule);
        waitingPeople = Map.of(true, new HashSet<>(), false, new HashSet<>());
        socket = new DatagramSocket(config.getIntProperty("floorPort") + floorNumber);
        receiveThread = new Thread(() -> {
            try {
                StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                        1, input -> getWaitingPeople((boolean) input.get(0)),
                        20, input -> {
                            interrupt();
                            return new AckMessage();
                        }));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method to run the thread
     */
    @Override
    public void run() {
        receiveThread.start();
        //currentTimeMillis use the time Of the underlying operating system and therefore will be adjusted automatically using NTP.
        long startTime = System.currentTimeMillis();
        while (!schedule.isEmpty() && !Thread.interrupted()) {
            if (System.currentTimeMillis() - startTime >= schedule.peek().getTimeToEvent()) {
                Event event = schedule.remove();
                try {
                    toggleButton(event.isFloorButtonIsUp(), true);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                waitingPeople.get(event.isFloorButtonIsUp()).add(event.getCarButton());
                try {
                    scheduler.handleFloorButton(new Destination(this.floorNumber, event.isFloorButtonIsUp()));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(schedule.peek().getTimeToEvent() - System.currentTimeMillis() + startTime);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    abstract protected void printTimes();

    /**
     * Interrupt method
     */
    @Override
    public void interrupt() {
        receiveThread.interrupt();
        printTimes();
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    /**
     * Gets the waiting people
     *
     * @return the set of floors with people waiting to go up
     */
    @Override
    public HashSet<Integer> getWaitingPeople(boolean isUp) {
        logger.info("Loading people from floor " + floorNumber + " onto the elevator going " + (isUp ? "up" : "down"));
        try {
            toggleButton(isUp, false);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        HashSet<Integer> output = new HashSet<>(waitingPeople.get(isUp));
        waitingPeople.get(isUp).clear();
        return output;
    }

    /**
     * Gets the floor number
     *
     * @return the floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }


    /**
     * Checks if there are events
     *
     * @return true if there are events
     */
    public boolean hasEvents() {
        return !schedule.isEmpty();
    }

    /**
     * Returns the schedule
     *
     * @return The schedule
     */
    public PriorityQueue<Event> getSchedule() {
        return schedule;
    }

    /**
     * Abstract method for turning the up button off
     */
    public abstract void toggleButton(boolean isUp, boolean on) throws IOException, ClassNotFoundException;

    /**
     * Abstract method for getting the down button
     */
    public abstract FloorButton getBottom();

    /**
     * Abstract method for getting the up button
     */
    public abstract FloorButton getTop();
}

/**
 * Representing the top floor
 */
class TopFloor extends Floor {
    private final FloorButton downButton;

    /**
     * Constructor for TopFloor
     *
     * @param config      the config file
     * @param floorNumber The floor number
     * @param scheduler   The scheduler
     * @param gui         the gui
     * @param schedule    The list of scheduled events
     */
    public TopFloor(Config config, int floorNumber, SchedulerApi scheduler, GuiApi gui, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        downButton = new FloorButton(floorNumber, false, gui);
    }

    @Override
    protected void printTimes() {
        System.out.println("Floor: " + getFloorNumber() + ", down on time: " + downButton.getOnTime());
        System.out.println("Floor: " + getFloorNumber() + ", down off time: " + downButton.getOffTime());
    }

    /**
     * Toggles the floor button
     *
     * @param isUp the floor button's direction
     * @param on   whether button is now on or off
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void toggleButton(boolean isUp, boolean on) throws IOException, ClassNotFoundException {
        if (!isUp) {
            downButton.setOn(on);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Method for getting the down button
     *
     * @return the button
     */
    @Override
    public FloorButton getBottom() {
        return downButton;
    }

    /**
     * Method for getting the top button
     *
     * @return the button
     */
    @Override
    public FloorButton getTop() {
        throw new RuntimeException();
    }
}

/**
 * Represents the bottom floor
 */
class BottomFloor extends Floor {
    private final FloorButton upButton;

    /**
     * Constructor for BottomFloor
     *
     * @param floorNumber The floor number
     * @param scheduler   The scheduler
     * @param schedule    The list of scheduled events
     */
    public BottomFloor(Config config, int floorNumber, SchedulerApi scheduler, GuiApi gui, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        upButton = new FloorButton(floorNumber, true, gui);
    }

    @Override
    protected void printTimes() {
        System.out.println("Floor: " + getFloorNumber() + ", up on time: " + upButton.getOnTime());
        System.out.println("Floor: " + getFloorNumber() + ", up off time: " + upButton.getOffTime());
    }

    /**
     * Toggles the floor button
     *
     * @param isUp the floor button's direction
     * @param on   whether the button should be on or off
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void toggleButton(boolean isUp, boolean on) throws IOException, ClassNotFoundException {
        if (isUp) {
            upButton.setOn(on);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Method for getting the bottom button
     *
     * @return the button
     */
    @Override
    public FloorButton getBottom() {
        throw new RuntimeException();
    }

    /**
     * Method for getting the up button
     *
     * @return the button
     */
    @Override
    public FloorButton getTop() {
        return upButton;
    }
}

/**
 * Representing the middle floor
 */
class MiddleFloor extends Floor {
    private final FloorButton upButton;
    private final FloorButton downButton;

    /**
     * Constructor for MiddleFloor
     *
     * @param config      The config file
     * @param floorNumber The floor number
     * @param scheduler   The scheduler
     * @param gui         The gui
     * @param schedule    The list of scheduled events
     */
    public MiddleFloor(Config config, int floorNumber, SchedulerApi scheduler, GuiApi gui, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        upButton = new FloorButton(floorNumber, true, gui);
        downButton = new FloorButton(floorNumber, false, gui);
    }

    @Override
    protected void printTimes() {
        System.out.println("Floor: " + getFloorNumber() + ", down on time: " + downButton.getOnTime());
        System.out.println("Floor: " + getFloorNumber() + ", down off time: " + downButton.getOffTime());
        System.out.println("Floor: " + getFloorNumber() + ", up on time: " + upButton.getOnTime());
        System.out.println("Floor: " + getFloorNumber() + ", up off time: " + upButton.getOffTime());
    }

    /**
     * Method for toggling the floor button
     *
     * @param isUp the button's direction
     * @param on   whether the button is on or off
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void toggleButton(boolean isUp, boolean on) throws IOException, ClassNotFoundException {
        if (isUp) {
            upButton.setOn(on);
        } else {
            downButton.setOn(on);
        }
    }

    /**
     * Method for returning the down button
     *
     * @return the downButton
     */
    @Override
    public FloorButton getBottom() {
        return downButton;
    }

    /**
     * Method for returning the up button
     *
     * @return the upButton
     */
    @Override
    public FloorButton getTop() {
        return upButton;
    }
}