package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Floor implements Runnable {
    private final Scheduler scheduler;
    private final List<Event> schedule;
    private final ScheduledExecutorService executor;
    private final FloorLamp upLamp;
    private final FloorLamp downLamp;

    public Floor(Scheduler scheduler, List<Event> schedule) {
        this.scheduler = scheduler;
        this.schedule = schedule;
        executor = Executors.newSingleThreadScheduledExecutor();
        upLamp = new FloorLamp();
        downLamp = new FloorLamp();
    }

    private static void runEvent(Event event) {
        System.out.println(event);
    }

    @Override
    public void run() {
        for (Event event : schedule) {
            long seconds_to_task = Duration.between(FloorSubsystem.getStartDate().toInstant(), event.getTime().toInstant()).getSeconds();
            executor.schedule(() -> Floor.runEvent(event), seconds_to_task, TimeUnit.SECONDS);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}


class TopFloor extends Floor{
    private final FloorButton downButton;

    public TopFloor(Scheduler scheduler, List<Event> schedule) {
        super(scheduler, schedule);
        downButton = new FloorButton();
    }
}

class BottomFloor extends Floor{
    private final FloorButton upButton;

    public BottomFloor(Scheduler scheduler, List<Event> schedule) {
        super(scheduler, schedule);
        upButton = new FloorButton();
    }
}

class MiddleFloor extends Floor{
    private final FloorButton upButton;
    private final FloorButton downButton;

    public MiddleFloor(Scheduler scheduler, List<Event> schedule) {
        super(scheduler, schedule);
        upButton = new FloorButton();
        downButton = new FloorButton();
    }
}