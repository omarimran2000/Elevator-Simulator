package FaultTestClasses;

import ElevatorSubsystem.ElevatorSubsystem;
import SchedulerSubsystem.SchedulerApi;
import stub.SchedulerClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;

public class ElevatorSubsystemOverride extends ElevatorSubsystem {

    public static void main(String[] args) throws IOException {
        TestConfig config = new TestConfig();
        config.addProperty("probabilityDoorStuck", "0.9");
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, config.getIntProperty("numFloors"));
    }
}