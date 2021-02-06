package SchedulerSubsystem;

import java.util.Date;

public class Event {
    private final Date time;
    private final int floor;
    private final boolean floorButtonIsUp;
    private final int carButton;


    /**
     * Constructor for Event
     * The user presses the button on the floor to request an elevator then enters the elevator and requests a new floor
     *
     * @param time The at which the user requests the elevator
     * @param floor The floor where the user starts
     * @param floor_button_is_up The direction of the elevator
     * @param car_button The button pressed inside the elevator
     */
    public Event(Date time, int floor, boolean floor_button_is_up, int car_button) {
        this.time = time;
        this.floor = floor;
        this.floorButtonIsUp = floor_button_is_up;
        this.carButton = car_button;
    }

    /**
     * Getter for the elevator car button
     * The button represents the requested floor
     * @return The button pressed in the elevator
     */
    public int getCarButton() {
        return carButton;
    }

    /**
     *
     * @return
     */
    public boolean isFloorButtonIsUp() {
        return floorButtonIsUp;
    }

    /**
     *
     * @return The current floor
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Getter for the time of the event
     * @return Time of the start of the event
     */
    public Date getTime() {
        return time;
    }

    /**
     * @return A string representation of the event
     */
    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", floor=" + floor +
                ", floorButtonIsUp=" + floorButtonIsUp +
                ", carButton=" + carButton +
                '}';
    }
}
