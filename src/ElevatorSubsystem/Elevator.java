package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

public class Elevator implements Runnable {

    private final Scheduler scheduler;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {

    }

}
