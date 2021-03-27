import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorApi;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Scheduler;
import SchedulerSubsystem.SchedulerApi;
import org.junit.jupiter.api.Test;
import stub.FloorClient;
import stub.SchedulerClient;
import utill.TestConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static FloorSubsystem.FloorSubsystem.generateFloors;
import static org.junit.jupiter.api.Assertions.*;

class FaultTest {

    @Test
    void DoorFaultTest() throws IOException, ParseException {

        TestConfig config = new TestConfig();
        config.addDictElem("probabilityDoorStuck","1");
        FloorSubsystem fs = new FloorSubsystem();
        ElevatorSubsystem es = new ElevatorSubsystem();
        Scheduler sched = new Scheduler(config);

        sched.main(null);
        es.main(null);
        fs.main(null);




    }


}
