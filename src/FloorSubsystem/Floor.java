package FloorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import model.Event;
import model.SendSet;
import utill.Config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.abs;

/**
 * The Floor class represents a single floor in the building
 *
 * @version Feb 27, 2021
 */
public abstract class Floor implements Runnable, FloorApi {

    private final SchedulerApi scheduler;
    private final PriorityQueue<Event> schedule;
    private final Set<Integer> waitingPeopleUp;
    private final Set<Integer> waitingPeopleDown;
    private final FloorLamp upLamp;
    private final FloorLamp downLamp;

    private final int floorNumber;
    private final Queue<Integer> destinationFloorNumbers;

    /**
     * Constructor
     *
     * @param floorNumber
     * @param scheduler
     * @param schedule    A list of events
     */
    public Floor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) {
        this.floorNumber = floorNumber;
        this.scheduler = scheduler;
        this.schedule = new PriorityQueue<>();
        this.schedule.addAll(schedule);
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();

        destinationFloorNumbers = new LinkedList<>();

        schedule.forEach(event -> {
            try {
                event.setTimeToEvent(abs((new SimpleDateFormat(config.getProperty("dateFormatPattern"))).parse(config.getProperty("startDate")).getTime() - event.getTime().getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        waitingPeopleUp = new HashSet<>();
        waitingPeopleDown = new HashSet<>();
    }

    /**
     * Method to run the thread
     */
    @Override
    public void run() {
        //currentTimeMillis use the time Of the underlying operating system and therefore will be adjusted automatically using NTP.
        long startTime = System.currentTimeMillis();
        while (!schedule.isEmpty()) {
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
                    scheduler.handleFloorButton(this.floorNumber, event.isFloorButtonIsUp());
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

    /**
     * @return the set of floors with people waiting to go up
     */
    public SendSet getWaitingPeopleUp() {
        turnUpButtonOff();
        SendSet waitingPeople = (SendSet) Set.copyOf(waitingPeopleUp);
        waitingPeopleUp.clear();
        return waitingPeople;
    }

    /**
     * @return the set of floors with people waiting to go down
     */
    public SendSet getWaitingPeopleDown() {
        turnDownButtonOff();
        Set<Integer> waitingPeople = Set.copyOf(waitingPeopleDown);
        waitingPeopleDown.clear();
        return (SendSet) waitingPeople;
    }

    /**
     * @return true if there are people waiting for an elevator
     */
    public boolean hasPeopleWaiting() {
        return !destinationFloorNumbers.isEmpty();
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
    public TopFloor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) {
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
        return null;
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
    public BottomFloor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) {
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
        return null;
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
    public MiddleFloor(Config config, int floorNumber, SchedulerApi scheduler, List<Event> schedule) {
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