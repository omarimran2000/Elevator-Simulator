package model;

import java.util.Date;
import java.util.Objects;

import static java.lang.Math.abs;


/**
 * The Event class represents the events that occur
 *
 * @version Feb 27, 2021
 */
public class Event implements Comparable<Event> {
    private final int floorNumber;
    private final boolean floorButtonIsUp;
    private final int carButton;
    private final long timeToEvent;

    /**
     * Constructor for Event
     * The user presses the button on the floor to request an elevator then enters the elevator and requests a new floor
     *
     * @param time               The at which the user requests the elevator
     * @param floorNumber        The floor where the user starts
     * @param floor_button_is_up The direction of the elevator
     * @param car_button         The button pressed inside the elevator
     */
    public Event(Date startTime, Date time, int floorNumber, boolean floor_button_is_up, int car_button) {
        this.timeToEvent = abs(startTime.getTime() - time.getTime());
        this.floorNumber = floorNumber;
        this.floorButtonIsUp = floor_button_is_up;
        this.carButton = car_button;
    }

    public Event(long timeToEvent, int floorNumber, boolean floor_button_is_up, int car_button) {
        this.timeToEvent = timeToEvent;
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

    @Override
    public String toString() {
        return "Event{" +
                "floorNumber=" + floorNumber +
                ", floorButtonIsUp=" + floorButtonIsUp +
                ", carButton=" + carButton +
                ", timeToEvent=" + timeToEvent +
                '}';
    }

    /**
     * Compare to event for the priority queue
     *
     * @param event the event to compare to
     * @return an integer representing if it is bigger or not
     */
    @Override
    public int compareTo(Event event) {
        return Long.compare(timeToEvent, event.timeToEvent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return floorNumber == event.floorNumber && floorButtonIsUp == event.floorButtonIsUp && carButton == event.carButton && timeToEvent == event.timeToEvent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floorNumber, floorButtonIsUp, carButton, timeToEvent);
    }
}
