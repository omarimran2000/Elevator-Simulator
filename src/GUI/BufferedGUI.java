package GUI;

import model.Destination;
import model.ElevatorState;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Keeps a queue of calls to the GUI
 * @version April 4, 2021
 */
public class BufferedGUI implements GuiApi, Runnable {
    private final GuiApi gui;
    private final ConcurrentLinkedQueue<Runnable> buffer;

    /**
     * Constructor for BufferedGui
     * @param gui The GUI
     */
    public BufferedGUI(GuiApi gui) {
        this.gui = gui;
        buffer = new ConcurrentLinkedQueue<>();
    }

    /**
     * Set the current floor number of the specified elevator
     *
     * @param elevatorNumber The specified elevator's number
     * @param floorNumber The current floor number
     */
    @Override
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) {
        buffer.add(() -> {
            try {
                gui.setCurrentFloorNumber(elevatorNumber, floorNumber);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * Set the specified elevator's direction
     *
     * @param elevatorNumber The specified elevator's number
     * @param direction is true for up, false for down
     */
    @Override
    public void setMotorDirection(int elevatorNumber, boolean direction) {
        buffer.add(() -> {
            try {
                gui.setMotorDirection(elevatorNumber, direction);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * Set the specified elevator's doors open/closed
     *
     * @param elevatorNumber The specified elevator's number
     * @param open is true for open, false for closed
     */
    @Override
    public void setDoorsOpen(int elevatorNumber, boolean open) {
        buffer.add(() -> {
            try {
                gui.setDoorsOpen(elevatorNumber, open);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * Set the specified elevator's state - (NotMoving, MovingUp, MovingDown, Stuck)
     *
     * @param elevatorNumber The specified elevator's number
     * @param state The elevator's state
     */
    @Override
    public void setState(int elevatorNumber, ElevatorState state) {
        buffer.add(() -> {
            try {
                gui.setState(elevatorNumber, state);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * Set the specified elevator's doors stuck
     *
     * @param elevatorNumber The specified elevator's number
     * @param doorsStuck is true if the doors are stuck, false if not
     * @param open is true if the doors are stuck open, false for closed
     */
    @Override
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) {
        buffer.add(() -> {
            try {
                gui.setDoorsStuck(elevatorNumber, doorsStuck, open);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

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
    @Override
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean isButton, boolean on) {
        buffer.add(() -> {
            try {
                gui.setElevatorButton(elevatorNumber, floorNumber, isButton, on);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * Show that the floorButton on the specified floor is on/off
     * @param floorNumber The specified floor's number
     * @param direction is true if the up button is on/off, false for down
     * @param on is true if the button is on, false for off
     */
    @Override
    public void setFloorButton(int floorNumber, boolean direction, boolean on) {
        buffer.add(() -> {
            try {
                gui.setFloorButton(floorNumber, direction, on);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * If an event is not able to be allocated to an elevator right away,
     * it is added to a list kept in the scheduler. Light up the specified
     * label in the schedulerPanel when a destination is added to this list
     * @param floorNumber The floor number
     * @param isUp is true of the event is up, false for down
     */
    @Override
    public void addSchedulerDestination(int floorNumber, boolean isUp) {
        buffer.add(() -> {
            try {
                gui.addSchedulerDestination(floorNumber, isUp);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * When some destinations are allocated to an elevator from the scheduler's
     * list, remove the light from the schedulerPanel
     * @param destinations The destination(s) being removed
     */
    @Override
    public void removeSchedulerDestinations(HashSet<Destination> destinations) {
        buffer.add(() -> {
            try {
                gui.removeSchedulerDestinations(destinations);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        });
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (buffer.peek() != null) {
                buffer.poll().run();
            }
        }
    }
}
