package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

public class Elevator implements Runnable {

    private final Scheduler scheduler;
    private final Door door;
    private final ArrivalSensor arrivalSensor;
    private final Motor motor;
    private final ArrayList<ElevatorButton> buttons;
    private final ArrayList<ElevatorLamp> elevatorLamps;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
        door = new Door();
        arrivalSensor = new ArrivalSensor();
        motor = new Motor();
        buttons = new ArrayList<>();
        elevatorLamps = new ArrayList<>();
    }

    @Override
    public void run() {

    }

}
