package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

public class Elevator implements Runnable {

    private final Scheduler scheduler;
    private Door door;
    private ArrivalSensor arrivalSensor;
    private Motor motor;
    private ArrayList<ElevatorButton> buttons;
    private ArrayList<ElevatorLamp> elevatorLamps;

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
