package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

public class ElevatorSubsystem {

    public ElevatorSubsystem(int numElevators, Scheduler scheduler) {
        for (int i = 0; i < numElevators; i++) {
            new Thread(new Elevator(scheduler), "Elevator " + (i + 1)).start();
        }
    }
}
