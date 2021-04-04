package GUI;

import model.Destination;
import model.ElevatorState;

import java.io.IOException;
import java.util.HashSet;

/**
 * API for the GUI
 * @version April 4, 2021
 */
public interface GuiApi {
    /**
     * Set the current floor number of the specified elevator
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The current floor number
     */
    void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException;

    /**
     * Set the specified elevator's direction
     *
     * @param elevatorNumber The specified elevator's number
     * @param direction is true for up, false for down
     */
    void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException;

    /**
     * Set the specified elevator's doors open/closed
     *
     * @param elevatorNumber The specified elevator's number
     * @param open is true for open, false for closed
     */
    void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException;

    /**
     * Set the specified elevator's state - (NotMoving, MovingUp, MovingDown, Stuck)
     *
     * @param elevatorNumber The specified elevator's number
     * @param state The elevator's state
     */
    void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException;

    /**
     * Set the specified elevator's doors stuck
     *
     * @param elevatorNumber The specified elevator's number
     * @param doorsStuck is true if the doors are stuck, false if not
     * @param open is true if the doors are stuck open, false for closed
     */
    void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException;

    /**
     * Show that a destination has been added to the specified elevator's queue
     * If the destination came from the scheduler, the floor lights up in blue
     * If the destination came from an elevatorButton pressed, the floor lights up in green
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The floor number
     * @param isButton is true if an elevatorButton was pressed, false if the scheduler added the destination
     * @param on is true if the destination is being added to the queue, false if it is being removed
     */
    void setElevatorButton(int elevatorNumber, int floorNumber, boolean isButton, boolean on) throws IOException, ClassNotFoundException;

    /**
     * Show that the floorButton on the specified floor is on/off
     * @param floorNumber The specified floor's number
     * @param direction is true if the up button is on/off, false for down
     * @param on is true if the button is on, false for off
     */
    void setFloorButton(int floorNumber, boolean direction, boolean on) throws IOException, ClassNotFoundException;

    /**
     * If an event is not able to be allocated to an elevator right away,
     * it is added to a list kept in the scheduler. Light up the specified
     * label in the schedulerPanel when a destination is added to this list
     * @param floorNumber The floor number
     * @param isUp is true of the event is up, false for down
     */
    void addSchedulerDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException;

    /**
     * When some destinations are allocated to an elevator from the scheduler's
     * list, remove the light from the schedulerPanel
     * @param destinations The destination(s) being removed
     */
    void removeSchedulerDestinations(HashSet<Destination> destinations) throws IOException, ClassNotFoundException;
}
