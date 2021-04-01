package GUI;

import java.io.IOException;

public interface GuiApi {
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber) throws IOException, ClassNotFoundException;
    public void setMotorDirection(int elevatorNumber, boolean direction) throws IOException, ClassNotFoundException;
    public void setDoorsOpen(int elevatorNumber, boolean open) throws IOException, ClassNotFoundException;
    public void setState(int elevatorNumber, String state) throws IOException, ClassNotFoundException;
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open) throws IOException, ClassNotFoundException;
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on) throws IOException, ClassNotFoundException;
    public void setFloorButton(int floorNumber, boolean direction, boolean on) throws IOException, ClassNotFoundException;
}
