package FloorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import model.AckMessage;
import model.Destination;
import model.Event;
import model.Floors;
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
    private final Set<Integer> waitingPeopleUp;
    private final Set<Integer> waitingPeopleDown;
    private final FloorLamp upLamp;
    private final FloorLamp downLamp;

    private final int floorNumber;
    private final DatagramSocket socket;
    private final Config config;

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
        this.config = config;
        this.schedule = new PriorityQueue<>(schedule);
        waitingPeopleUp = new HashSet<>();
        waitingPeopleDown = new HashSet<>();
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();
        socket = new DatagramSocket(config.getIntProperty("floorPort") + floorNumber);
    }

    /**
     * Method to run the thread
     */
    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            try {
                StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                        1, input -> getWaitingPeopleUp(),
                        2, input -> getWaitingPeopleDown(),
                        20, input -> {interrupt(); return new AckMessage();}  ) );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        //currentTimeMillis use the time Of the underlying operating system and therefore will be adjusted automatically using NTP.
        long startTime = System.currentTimeMillis();
        while (!schedule.isEmpty() && !Thread.interrupted()) {
            if (System.currentTimeMillis() - startTime >= schedule.peek().getTimeToEvent()) {
                Event event = schedule.remove();
                if (event.isFloorButtonIsUp()) {
                    turnUpButtonOn();
                    waitingPeopleUp.add(event.getCarButton());
                } else {
                    turnDownButtonOn();
                    waitingPeopleDown.add(event.getCarButton());
                }
                try {
                    scheduler.handleFloorButton(new Destination(this.floorNumber, event.isFloorButtonIsUp()));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(schedule.peek().getTimeToEvent() - System.currentTimeMillis() + startTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

     @Override
    public void interrupt() {
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    /**
     * @return the set of floors with people waiting to go up
     */
    public Floors getWaitingPeopleUp() {
        logger.info("Loading people onto the elevator going up");
        turnUpButtonOff();
        Floors waitingPeople = new Floors(waitingPeopleUp);
        waitingPeopleUp.clear();
        return waitingPeople;
    }

    /**
     * @return the set of floors with people waiting to go down
     */
    public Floors getWaitingPeopleDown() {
        logger.info("Loading people onto the elevator going down");
        turnDownButtonOff();
        Floors waitingPeople = new Floors(waitingPeopleDown);
        waitingPeopleDown.clear();
        return waitingPeople;
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
    public abstract void turnUpButtonOff();

    /**
     * Abstract method for turning the up button on
     */
    public abstract void turnUpButtonOn();

    /**
     * Abstract method for turning the down button off
     */
    public abstract void turnDownButtonOff();

    /**
     * Abstract method for turning the down button on
     */
    public abstract void turnDownButtonOn();

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
    public TopFloor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        downButton = new FloorButton(floorNumber, false);
    }

    @Override
    public void turnUpButtonOff() {

    }

    @Override
    public void turnUpButtonOn() {

    }

    /**
     * Method for turning the down button off
     */
    public void turnDownButtonOff() {
        downButton.setOn(false);
    }

    /**
     * Method for turning the down button on
     */
    public void turnDownButtonOn() {
        downButton.setOn(true);
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
    public BottomFloor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        upButton = new FloorButton(floorNumber, true);
    }

    /**
     * Method for turning the up button off
     */
    public void turnUpButtonOff() {
        upButton.setOn(false);
    }

    /**
     * Method for turning the up button on
     */
    public void turnUpButtonOn() {
        upButton.setOn(true);
    }

    @Override
    public void turnDownButtonOff() {

    }

    @Override
    public void turnDownButtonOn() {

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
    public MiddleFloor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) throws SocketException {
        super(config, floorNumber, scheduler, schedule);
        upButton = new FloorButton(floorNumber, true);
        downButton = new FloorButton(floorNumber, false);
    }

    /**
     * Method for turning the down button on
     */
    public void turnDownButtonOff() {
        downButton.setOn(false);
    }

    /**
     * Method for turning the down button off
     */
    public void turnDownButtonOn() {
        downButton.setOn(true);
    }

    /**
     * Method for turning the up button off
     */
    public void turnUpButtonOff() {
        upButton.setOn(false);
    }

    /**
     * Method for turning the up button on
     */
    public void turnUpButtonOn() {
        upButton.setOn(true);
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