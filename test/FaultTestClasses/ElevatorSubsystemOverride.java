package FaultTestClasses;

import ElevatorSubsystem.ElevatorSubsystem;
import GUI.GuiApi;
import SchedulerSubsystem.SchedulerApi;
import stub.GuiClient;
import stub.SchedulerClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;

public class ElevatorSubsystemOverride extends ElevatorSubsystem {

    //Overriding of the main class allows us to make changes to config file
    //and then runt the tests with the TestConfig
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TestConfig config = new TestConfig();
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        GuiApi guiApi = new GuiClient(config, InetAddress.getLocalHost(), config.getIntProperty("GUIPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, guiApi, config.getIntProperty("numFloors"));
    }
}
