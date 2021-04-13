package model;

import java.util.Date;

import static java.lang.Math.abs;


/**
 * The Event class represents the events that occur
 *
 * @param timeToEvent     Getter for the time to the event
 * @param floorNumber     The floor where the user starts
 * @param floorButtonIsUp The direction of the elevator
 * @param carButton       The button pressed inside the elevator
 */
public record Event(long timeToEvent, int floorNumber, boolean floorButtonIsUp,
                    int carButton) implements Comparable<Event> {

    /**
     * Constructor for Event
     * The user presses the button on the floor to request an elevator then enters the elevator and requests a new floor
     *
     * @param time            The at which the user requests the elevator
     * @param floorNumber     The floor where the user starts
     * @param floorButtonIsUp The direction of the elevator
     * @param carButton       The button pressed inside the elevator
     */
    public Event(Date startTime, Date time, int floorNumber, boolean floorButtonIsUp, int carButton) {
        this(abs(startTime.getTime() - time.getTime()), floorNumber, floorButtonIsUp, carButton);
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
}
