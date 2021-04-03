package FaultTestClasses;

import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;
import GUI.GuiApi;
import SchedulerSubsystem.SchedulerApi;
import stub.GuiClient;
import stub.SchedulerClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Map;

public class FloorSubsystemOverride extends FloorSubsystem {
    //Overriding of the main class allows us to make changes to config file
    //and then runt the tests with the TestConfig
    public static void main(String[] args) throws IOException, ParseException {
        TestConfig config = new TestConfig();
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        GuiApi guiApi = new GuiClient(config, InetAddress.getLocalHost(), config.getIntProperty("GUIPort"));
        Map<Integer, Floor> floors = generateFloors(config, schedulerApi, guiApi, config.getProperty("csvFileName"));
        floors.forEach((floorNumber, floor) -> floor.start());
    }
}
