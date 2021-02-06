package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.lang.Math.abs;


public abstract class Floor implements Runnable {
    private static final boolean skipDuration = true; //FIXME move to a config file.
    private final Scheduler scheduler;
    private final PriorityQueue<Event> schedule;
   // private final ScheduledExecutorService executor;
    private final FloorLamp upLamp;
    private final FloorLamp downLamp;

    private final int floorNumber;
    private final Queue<Integer> destinationFloorNumbers;
    private int numEvents;

    /**
     * Constructor
     * @param floorNumber
     * @param scheduler
     * @param schedule A list of events
     */
    public Floor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        this.floorNumber = floorNumber;
        this.scheduler = scheduler;
        this.schedule = new PriorityQueue<>();
        this.schedule.addAll(schedule);
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();

        destinationFloorNumbers = new LinkedList<>();
        numEvents = schedule.size();
        for (Event event : schedule) {
            event.setFloor(this);
            long seconds_to_task = abs(FloorSubsystem.getStartDate().getTime()- event.getTime().getTime())/1000;
            event.setTimeToEvent(seconds_to_task);
            scheduler.addToQueue(event);

        }
    }

    @Override
    public void run() {
        while(scheduler.hasEvents()) {
            if(!schedule.isEmpty() && schedule.peek().getTimeToEvent()<=scheduler.getTimePassed()&&scheduler.priorityEvent().equals(schedule.peek()))
            {
                this.turnButtonOn();
                moveElevator(schedule.peek().getCarButton());
                scheduler.removeEvent(schedule.peek());
                schedule.poll();
            }

        }
    }
    public void moveElevator(int carButton){
        scheduler.moveElevatorToFloorNumber(this.floorNumber,carButton);

    }


    /**
     * @return true if there are people waiting for an elevator
     */
    public boolean hasPeopleWaiting() {
        return !destinationFloorNumbers.isEmpty();
    }


    /**
     * @return true if there are events
     */
    public boolean hasEvents() {
        return ! schedule.isEmpty();
    }
    public abstract void turnButtonOff();
    public abstract void turnButtonOn();
}


class TopFloor extends Floor {
    private final FloorButton downButton;

    /**
     * Constructor for TopFloor
     * @param floorNumber The floor number
     * @param scheduler The scheduler
     * @param schedule The list of scheduled events
     */
    public TopFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        downButton = new FloorButton();
    }
    public void turnButtonOff()
    {
        downButton.setOn(false);
    }
    public void turnButtonOn()
    {
        downButton.setOn(true);
    }
}

class BottomFloor extends Floor {
    private final FloorButton upButton;

    /**
     * Constructor for BottomFloor
     * @param floorNumber The floor number
     * @param scheduler The scheduler
     * @param schedule The list of scheduled events
     */
    public BottomFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        upButton = new FloorButton();
    }
    public void turnButtonOff()
    {
        upButton.setOn(false);
    }
    public void turnButtonOn()
    {
        upButton.setOn(true);
    }
}

class MiddleFloor extends Floor {
    private final FloorButton upButton;
    private final FloorButton downButton;

    /**
     * Constructor for MiddleFloor
     * @param floorNumber The floor number
     * @param scheduler The scheduler
     * @param schedule The list of scheduled events
     */
    public MiddleFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        upButton = new FloorButton();
        downButton = new FloorButton();
    }
    public void turnButtonOff()
    {
        downButton.setOn(false);
        upButton.setOn(false);
    }
    public void turnButtonOn()
    {
        downButton.setOn(true);
        upButton.setOn(true);
    }
}