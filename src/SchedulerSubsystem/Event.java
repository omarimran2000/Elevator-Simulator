package SchedulerSubsystem;

import java.util.Date;


/**
 * The Event class represents the events that occur
 *
 * @version Feb 06, 2021
 */
public class Event implements Comparable<Event> {
    private final Date time;
    private final int floorNumber;
    private final boolean floorButtonIsUp;
    private final int carButton;
    private long timeToEvent;

    /**
     * Constructor for Event
     * The user presses the button on the floor to request an elevator then enters the elevator and requests a new floor
     *
     * @param time               The at which the user requests the elevator
     * @param floorNumber        The floor where the user starts
     * @param floor_button_is_up The direction of the elevator
     * @param car_button         The button pressed inside the elevator
     */
    public Event(Date time, int floorNumber, boolean floor_button_is_up, int car_button) {
        this.time = time;
        this.floorNumber = floorNumber;
        this.floorButtonIsUp = floor_button_is_up;
        this.carButton = car_button;

    }

    /**
     * Getter for the elevator car button
     * The button represents the requested floor
     *
     * @return The button pressed in the elevator
     */
    public int getCarButton() {
        return carButton;
    }

    /**
     * @return The current floor
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Getter for the time of the event
     *
     * @return Time of the start of the event
     */
    public Date getTime() {
        return time;
    }

    /**
     * Getter for floor button up
     *
     * @return is floor button up
     */
    public boolean isFloorButtonIsUp() {
        return floorButtonIsUp;
    }

    /**
     * Getter for the time to the event
     *
     * @return the time to the event
     */
    public long getTimeToEvent() {
        return timeToEvent;
    }

    /**
     * Setting the time to event for the event
     *
     * @param timeToEvent relative time
     */
    public void setTimeToEvent(long timeToEvent) {
        this.timeToEvent = timeToEvent;
    }

    /**
     * @return A string representation of the event
     */
    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", floorNumber=" + floorNumber +
                ", floorButtonIsUp=" + floorButtonIsUp +
                ", carButton=" + carButton +
                '}';
    }


    /**
     * Compare to method for the priority queue
     *
     * @param o the method to compare to
     * @return an integer representing if it is bigger or not
     */
    @Override
    public int compareTo(Event o) {
        long thisTime = this.time.getTime();
        long anotherTime = o.time.getTime();
        return (thisTime < anotherTime ? -1 : 1);
    }
}
