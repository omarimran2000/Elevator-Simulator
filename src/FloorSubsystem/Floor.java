package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

public abstract class Floor implements Runnable {
    private static final boolean skipDuration = true; //FIXME move to a config file.
    private final Scheduler scheduler;
    private final List<Event> schedule;
   // private final ScheduledExecutorService executor;
    private final FloorLamp upLamp;
    private final FloorLamp downLamp;

    private final int floorNumber;
    private final Queue<Integer> destinationFloorNumbers;
    private int numEvents;

    public Floor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        this.floorNumber = floorNumber;
        this.scheduler = scheduler;
        this.schedule = schedule;
        //executor = Executors.newSingleThreadScheduledExecutor();
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();

        destinationFloorNumbers = new LinkedList<>();
        numEvents = schedule.size();
        for (Event event : schedule) {
            long seconds_to_task = Duration.between(FloorSubsystem.getStartDate().toInstant(), event.getTime().toInstant()).getSeconds();
            //executor.schedule(() -> this.runEvent(event), seconds_to_task, TimeUnit.SECONDS);
            scheduler.addToQueue(event);

        }
    }

    private void runEvent(Event event) {
        System.out.println(event);
        destinationFloorNumbers.add(event.getCarButton());
        scheduler.moveElevatorToFloorNumber(event.getFloor());
        numEvents--;
    }

    @Override
    public void run() {
        while(scheduler.hasEvents()) {

        }
    }
    public void moveElevator(int carButton){
        scheduler.moveElevatorToFloorNumber(this.floorNumber);
        scheduler.moveElevatorToFloorNumber(carButton);

    }

    public Scheduler getScheduler()
    {
        return scheduler;
    }
    public List<Event> getSchedule()
    {
        return schedule;
    }

    public boolean hasPeopleWaiting() {
        return !destinationFloorNumbers.isEmpty();
    }

    public int getNextElevatorButton() {
        return destinationFloorNumbers.remove();
    }

    public boolean hasEvents() {
        return ! schedule.isEmpty();
    }
    public abstract void turnButtonOff();
    public abstract void turnButtonOn();
}


class TopFloor extends Floor {
    private final FloorButton downButton;

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