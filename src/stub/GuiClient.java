package stub;

import GUI.GuiApi;
import utill.Config;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

public class GuiClient extends StubClient implements GuiApi {
    private final InetAddress inetAddress;
    private final int port;

    /**
     * The default stub client constructor.
     *
     * @param config The application configuration file loader.
     */
    protected GuiClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber){

    }
    public void setMotorDirection(int elevatorNumber, boolean direction) {

    }
    public void setDoorsOpen(int elevatorNumber, boolean open){

    }
    public void setState(int elevatorNumber, String state){

    }
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open){

    }
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on){

    }
    public void setFloorButton(int floorButton, boolean direction, boolean on){

    }
}
