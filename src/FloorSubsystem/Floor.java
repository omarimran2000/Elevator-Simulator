package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.util.List;

public class Floor implements Runnable {
    private final Scheduler scheduler;
    private final List<Event> schedule;
    private FloorButton upButton;
    private FloorButton downButton;
    private FloorLamp upLamp;
    private FloorLamp downLamp;

    public Floor(Scheduler scheduler, List<Event> schedule,boolean topFloor,boolean bottomFloor) {
        this.scheduler = scheduler;
        this.schedule = schedule;
        if(!topFloor && !bottomFloor){
            upButton = new FloorButton();
            downButton = new FloorButton();

            upLamp = new FloorLamp();
            downLamp = new FloorLamp();
        }
        else if(bottomFloor)
        {
            upButton = new FloorButton();
            upLamp = new FloorLamp();
        }
        else
        {
            downButton = new FloorButton();
            downLamp = new FloorLamp();
        }
    }

    @Override
    public void run() {
    }
}
