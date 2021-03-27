package FaultTestClasses;

import ElevatorSubsystem.ElevatorApi;
import FloorSubsystem.FloorApi;
import SchedulerSubsystem.Scheduler;
import stub.ElevatorClient;
import stub.FloorClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerOverride extends Scheduler {

    public SchedulerOverride(TestConfig config) throws IOException {
        super(config);
    }

    public static void main(String[] args) throws IOException {
        TestConfig config = new TestConfig();
        config.addProperty("probabilityDoorStuck", "0.9");
        InetAddress localhost = InetAddress.getLocalHost();
        SchedulerOverride schedulerOverride = new SchedulerOverride(config);
        Map<Integer, FloorApi> floors = new HashMap<>();
        for (int i = 0; i < config.getIntProperty("numFloors"); i++) {
            floors.put(i, new FloorClient(config, localhost, config.getIntProperty("floorPort") + i));
        }
        schedulerOverride.setFloors(floors);
        List<ElevatorApi> elevatorClients = new ArrayList<>();
        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            elevatorClients.add(new ElevatorClient(config, localhost, config.getIntProperty("elevatorPort") + i));
        }
        schedulerOverride.setElevators(elevatorClients);

        schedulerOverride.start();
    }
}
