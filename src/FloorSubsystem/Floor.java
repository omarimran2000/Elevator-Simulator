package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Floor implements Runnable {
    private final Scheduler scheduler;
    private final List<Event> schedule;
    private final ScheduledExecutorService executor;
    private FloorButton upButton;
    private FloorButton downButton;
    private FloorLamp upLamp;
    private FloorLamp downLamp;

    public Floor(Scheduler scheduler, List<Event> schedule,boolean topFloor,boolean bottomFloor) {
        this.scheduler = scheduler;
        this.schedule = schedule;
        executor = Executors.newSingleThreadScheduledExecutor();
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
