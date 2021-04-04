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
 * @version Feb 27, 2021
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
     * @param floorNumber
     * @param scheduler
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

    @Override
    public void interrupt() {
        receiveThread.interrupt();
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    /**
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

    public int getFloorNumber() {
        return floorNumber;
    }


    /**
     * @return true if there are events
     */
    public boolean hasEvents() {
        return !schedule.isEmpty();
    }

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


class TopFloor extends Floor {
    private final FloorButton downButton;

    /**
     * Constructor for TopFloor
     *
     * @param floorNumber The floor number
     * @param scheduler   The scheduler
     * @param schedule    The list of scheduled events
     */
    public TopFloor(Config config, int floorNumber, SchedulerApi scheduler, GuiApi gui, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        downButton = new FloorButton(floorNumber, false, gui);
    }

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
     */
    @Override
    public FloorButton getBottom() {
        return downButton;
    }

    /**
     * Method for getting the top button
     */
    @Override
    public FloorButton getTop() {
        throw new RuntimeException();
    }
}

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
    public void toggleButton(boolean isUp, boolean on) throws IOException, ClassNotFoundException {
        if (isUp) {
            upButton.setOn(on);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Method for getting the bottom button
     */
    @Override
    public FloorButton getBottom() {
        throw new RuntimeException();
    }

    /**
     * Method for getting the up button
     */
    @Override
    public FloorButton getTop() {
        return upButton;
    }
}

class MiddleFloor extends Floor {
    private final FloorButton upButton;
    private final FloorButton downButton;

    /**
     * Constructor for MiddleFloor
     *
     * @param floorNumber The floor number
     * @param scheduler   The scheduler
     * @param schedule    The list of scheduled events
     */
    public MiddleFloor(Config config, int floorNumber, SchedulerApi scheduler, GuiApi gui, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        upButton = new FloorButton(floorNumber, true, gui);
        downButton = new FloorButton(floorNumber, false, gui);
    }


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