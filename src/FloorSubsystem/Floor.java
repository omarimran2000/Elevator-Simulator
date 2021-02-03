package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Floor implements Runnable {
    private static final boolean skipDuration = true; //FIXME move to a config file.
    private final Scheduler scheduler;
    private final List<Event> schedule;
    private final ScheduledExecutorService executor;
    private final FloorLamp upLamp;
    private final FloorLamp downLamp;
    private final int floorNumber;
    private final Queue<Integer> destinationFloorNumbers;
    private int numEvents;

    public Floor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        this.floorNumber = floorNumber;
        this.scheduler = scheduler;
        this.schedule = schedule;
        executor = Executors.newSingleThreadScheduledExecutor();
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();
        destinationFloorNumbers = new LinkedList<>();
        numEvents = schedule.size();
    }

    private void runEvent(Event event) {
        System.out.println(event);
        destinationFloorNumbers.add(event.getCarButton());
        scheduler.moveElevatorToFloorNumber(event.getFloor());
        numEvents--;
    }

    @Override
    public void run() {
        for (Event event : schedule) {
            long seconds_to_task = skipDuration ? 1 : Duration.between(FloorSubsystem.getStartDate().toInstant(), event.getTime().toInstant()).getSeconds();
            executor.schedule(() -> this.runEvent(event), seconds_to_task, TimeUnit.SECONDS);
        }
        while(scheduler.getNumEvents()>0)
        {

        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean hasPeopleWaiting() {
        return !destinationFloorNumbers.isEmpty();
    }

    public int getNextElevatorButton() {
        return destinationFloorNumbers.remove();
    }

    public int getNumEvents() {
        return numEvents;
    }
}


class TopFloor extends Floor {
    private final FloorButton downButton;

    public TopFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        downButton = new FloorButton();
    }
}

class BottomFloor extends Floor {
    private final FloorButton upButton;

    public BottomFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        upButton = new FloorButton();
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
}