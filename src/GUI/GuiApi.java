package GUI;

import model.ElevatorState;

import java.io.IOException;

public interface GuiApi {
    void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException;

    void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException;

    void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException;

    void setState(int elevatorNumber, ElevatorState state) throws IOException, ClassNotFoundException;

    void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException;

    void setElevatorButton(int elevatorNumber, int floorNumber, boolean on) throws IOException, ClassNotFoundException;

    void setFloorButton(int floorNumber, boolean direction, boolean on) throws IOException, ClassNotFoundException;

    //void setScheduler(String message) throws IOException, ClassNotFoundException;

    void setSchedulerMessage(String message) throws IOException, ClassNotFoundException;

    void setSchedulerDestinations(String destinations) throws IOException, ClassNotFoundException;
}
