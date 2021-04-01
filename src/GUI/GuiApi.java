package GUI;

public interface GuiApi {

    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber);
    public void setMotorDirection(int elevatorNumber, boolean direction);
    public void setDoorsOpen(int elevatorNumber, boolean open);
    public void setState(int elevatorNumber, String state);
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck);
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on);
    public void setFloorButton(int floorButton, boolean direction, boolean on);
}
