package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.util.List;

public class Floor implements Runnable {
    private final Scheduler scheduler;
    private final List<Event> schedule;

    public Floor(Scheduler scheduler, List<Event> schedule) {
        this.scheduler = scheduler;
        this.schedule = schedule;
    }

    @Override
    public void run() {
    }
}
