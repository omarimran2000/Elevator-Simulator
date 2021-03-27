package FaultTestClasses;

import ElevatorSubsystem.*;
import SchedulerSubsystem.SchedulerApi;
import stub.SchedulerClient;
import utill.TestConfig;
import java.io.IOException;
import java.net.InetAddress;

public class ElevatorSubsystemOverride extends ElevatorSubsystem {

    //Overriding of the main class allows us to make changes to config file
    //and then runt the tests with the TestConfig
    public static void main(String[] args) throws IOException {
        TestConfig config = new TestConfig();
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, config.getIntProperty("numFloors"));
    }
}
