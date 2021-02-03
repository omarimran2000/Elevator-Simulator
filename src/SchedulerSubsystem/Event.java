package SchedulerSubsystem;

import java.util.Date;

public class Event {
    private final Date time;
    private final int floor;
    private final boolean floor_button_is_up;
    private final int car_button;


    public Event(Date time, int floor, boolean floor_button_is_up, int car_button) {
        this.time = time;
        this.floor = floor;
        this.floor_button_is_up = floor_button_is_up;
        this.car_button = car_button;
    }

    public int getCar_button() {
        return car_button;
    }

    public boolean isFloor_button_is_up() {
        return floor_button_is_up;
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
                ", floor_button_is_up=" + floor_button_is_up +
                ", car_button=" + car_button +
                '}';
    }
}
