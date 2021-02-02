package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

public class Elevator implements Runnable {

    private Scheduler scheduler;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {

    }

}
