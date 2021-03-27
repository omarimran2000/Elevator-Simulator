package FaultTestClasses;

import ElevatorSubsystem.*;

import SchedulerSubsystem.Scheduler;
import SchedulerSubsystem.SchedulerApi;
import model.Destination;
import stub.SchedulerClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class ElevatorSubsystemOverride extends ElevatorSubsystem {

    public static void main(String[] args) throws IOException {
        TestConfig config = new TestConfig();
        config.addProperty("probabilityDoorStuck", "0.9");
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, config.getIntProperty("numFloors"));
    }
}
