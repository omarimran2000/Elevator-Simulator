package GUI;

import model.Destination;
import model.ElevatorState;

import java.io.IOException;
import java.util.HashSet;

public interface GuiApi {
    void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException;

    void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException;

    void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException;

    void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException;

    void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException;

    void setElevatorButton(int elevatorNumber, int floorNumber, boolean on) throws IOException, ClassNotFoundException;

    void setFloorButton(int floorNumber, boolean direction, boolean on) throws IOException, ClassNotFoundException;


    void addSchedulerDestination(int floorNumber, boolean isUp)throws IOException, ClassNotFoundException;

    void removeSchedulerDestinations(HashSet<Destination> destinations) throws IOException, ClassNotFoundException;
}
