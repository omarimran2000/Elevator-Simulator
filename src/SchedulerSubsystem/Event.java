package SchedulerSubsystem;

import java.util.Date;

public class Event implements Comparable<Event>{
    private final Date time;
    private final int floor;
    private final boolean floorButtonIsUp;
    private final int carButton;


    public Event(Date time, int floor, boolean floor_button_is_up, int car_button) {
        this.time = time;
        this.floor = floor;
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
        return floor;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", floor=" + floor +
                ", floorButtonIsUp=" + floorButtonIsUp +
                ", carButton=" + carButton +
                '}';
    }

    @Override
    public int compareTo(Event o) {
        return this.time.compareTo(o.time);
    }
}
