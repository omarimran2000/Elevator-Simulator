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

    /**
     * Constructor
     * @param floorNumber
     * @param scheduler
     * @param schedule A list of events
     */
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

    /**
     * Runs the specified event
     * @param event The event to be run
     */
    private void runEvent(Event event) {
        System.out.println(event.toString());
        destinationFloorNumbers.add(event.getCarButton());
        scheduler.moveElevatorToFloorNumber(event.getFloor());
        numEvents--;
    }

    /**
     * The run method
     *
     */
    @Override
    public void run() {
        for (Event event : schedule) {
            long seconds_to_task = skipDuration ? 1 : Duration.between(FloorSubsystem.getStartDate().toInstant(), event.getTime().toInstant()).getSeconds();
            executor.schedule(() -> this.runEvent(event), seconds_to_task, TimeUnit.SECONDS);
        }
    }

    /**
     * Shutdown the executor
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Getter for the floor number
     * @return The floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * @return true if there are people waiting for an elevator
     */
    public boolean hasPeopleWaiting() {
        return !destinationFloorNumbers.isEmpty();
    }

    /**
     * @return The next available elevator button
     */
    public int getNextElevatorButton() {
        return destinationFloorNumbers.remove();
    }

    /**
     * @return true if there are events
     */
    public boolean hasEvents() {
        return numEvents != 0;
    }
}


class TopFloor extends Floor {
    private final FloorButton downButton;

    /**
     * Constructor for TopFloor
     * @param floorNumber The floor number
     * @param scheduler The scheduler
     * @param schedule The list of scheduled events
     */
    public TopFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        downButton = new FloorButton();
    }
}

class BottomFloor extends Floor {
    private final FloorButton upButton;

    /**
     * Constructor for BottomFloor
     * @param floorNumber The floor number
     * @param scheduler The scheduler
     * @param schedule The list of scheduled events
     */
    public BottomFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        upButton = new FloorButton();
    }
}

class MiddleFloor extends Floor {
    private final FloorButton upButton;
    private final FloorButton downButton;

    /**
     * Constructor for MiddleFloor
     * @param floorNumber The floor number
     * @param scheduler The scheduler
     * @param schedule The list of scheduled events
     */
    public MiddleFloor(int floorNumber, Scheduler scheduler, List<Event> schedule) {
        super(floorNumber, scheduler, schedule);
        upButton = new FloorButton();
        downButton = new FloorButton();
    }
}