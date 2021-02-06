package SchedulerSubsystem;

import FloorSubsystem.Floor;

import java.util.Date;
import java.util.TimerTask;

public class Event implements Comparable<Event>{
    private final Date time;
    private final int floorInt;
    private Floor floor;
    private final boolean floorButtonIsUp;
    private final int carButton;
    private long timeToEvent;


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

    public void setTimeToEvent(long timeToEvent) {
        this.timeToEvent = timeToEvent;
    }

    public long getTimeToEvent() {
        return timeToEvent;
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
        long thisTime = this.time.getTime();
        long anotherTime = o.time.getTime();
        return (thisTime<anotherTime ? -1  : 1);
    }
    /*
    @Override
    public void run() {
        floor.moveElevator(carButton);
        floor.getScheduler().removeEvent(this);
    }

     */
}
