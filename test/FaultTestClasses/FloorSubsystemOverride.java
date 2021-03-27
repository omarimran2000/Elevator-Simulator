package FaultTestClasses;

import FloorSubsystem.*;
import SchedulerSubsystem.SchedulerApi;
import stub.SchedulerClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Map;

public class FloorSubsystemOverride extends FloorSubsystem {
    public FloorSubsystemOverride(){
        super();
    }

    public static void main(String[] args) throws IOException, ParseException {
        TestConfig config = new TestConfig();
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        Map<Integer, Floor> floors = generateFloors(config, schedulerApi, config.getProperty("csvFileName"));
        floors.forEach((floorNumber, floor) -> floor.start());
    }
}
