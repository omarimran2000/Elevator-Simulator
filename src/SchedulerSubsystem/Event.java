package SchedulerSubsystem;

import FloorSubsystem.Floor;

import java.util.Date;
import java.util.TimerTask;

public class Event extends TimerTask implements Comparable<Event>{
    private final Date time;
    private final int floorInt;
    private Floor floor;
    private final boolean floorButtonIsUp;
    private final int carButton;


    public Event(Date time, int floor, boolean floor_button_is_up, int car_button) {
        this.time = time;
        this.floorInt = floor;
        this.floorButtonIsUp = floor_button_is_up;
        this.carButton = car_button;
    }

    public int getCarButton() {
        return carButton;
    }

    public boolean isFloorButtonIsUp() {
        return floorButtonIsUp;
    }

    public int getFloor() {
        return floorInt;
    }

    public Date getTime() {
        return time;
    }

    public void setFloor(Floor floor)
    {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", floor=" + floorInt +
                ", floorButtonIsUp=" + floorButtonIsUp +
                ", carButton=" + carButton +
                '}';
    }

    @Override
    public int compareTo(Event o) {
        return this.time.compareTo(o.time);
    }

    @Override
    public void run() {
        floor.moveElevator(carButton);
        floor.getScheduler().removeEvent(this);
    }
}
