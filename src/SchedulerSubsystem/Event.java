package SchedulerSubsystem;

/**
 * The Event class represents the events that occur
 *
 * @version Feb 06, 2021
 */
public class Event {
    private final long secToEvent;
    private final int floorNumber;
    private final boolean floorButtonIsUp;
    private final int carButton;
    private long timeToEvent;

    /**
     * Constructor for Event
     * The user presses the button on the floor to request an elevator then enters the elevator and requests a new floor
     *
     * @param secToEvent         The number of seconds to the start of the event
     * @param floorNumber        The floor number where the user starts
     * @param floor_button_is_up The direction of the elevator
     * @param car_button         The button pressed inside the elevator
     */
    public Event(long secToEvent, int floorNumber, boolean floor_button_is_up, int car_button) {
        this.secToEvent = secToEvent;
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
     * @return The current floor number
     */
    public int getFloor() {
        return floorNumber;
    }

    /**
     * Getter for the number of seconds to the start of the event
     *
     * @return The number of seconds to the start of the event
     */
    public long getSecToEvent() {
        return secToEvent;
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
                "secToEvent=" + secToEvent +
                ", floorNumber=" + floorNumber +
                ", floorButtonIsUp=" + floorButtonIsUp +
                ", carButton=" + carButton +
                '}';
    }
}
