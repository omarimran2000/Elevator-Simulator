package GUI;

import model.Destination;
import model.ElevatorState;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferedGUI implements GuiApi, Runnable {
    private final GuiApi gui;
    private final ConcurrentLinkedQueue<Runnable> buffer;


    public BufferedGUI(GuiApi gui) {
        this.gui = gui;
        buffer = new ConcurrentLinkedQueue<>();
    }

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

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (buffer.peek() != null) {
                buffer.poll().run();
            }
        }
    }
}
