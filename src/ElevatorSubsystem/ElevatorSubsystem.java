package ElevatorSubsystem;

import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import java.util.ArrayList;

public class ElevatorSubsystem {

    private static int NUM_ELEVATORS;
    private Scheduler scheduler;
    private ArrayList<Thread> elevators;

    public ElevatorSubsystem(int numElevators,Scheduler scheduler) {
        this.NUM_ELEVATORS = numElevators;
        this.scheduler = scheduler;

        elevators = new ArrayList<>();

        for(int i=0;i<NUM_ELEVATORS;i++)
        {
            Thread temp = new Thread(new Elevator(scheduler),"Elevator "+(i+1));
            temp.start();
            elevators.add(temp);
        }
    }
}
