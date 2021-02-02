package FloorSubsystem;

import SchedulerSubsystem.Scheduler;

public class Floor implements Runnable {
    private Scheduler scheduler;

    public Floor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
    }
}
