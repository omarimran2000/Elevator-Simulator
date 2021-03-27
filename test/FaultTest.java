import ElevatorSubsystem.ElevatorSubsystem;
import FaultTestClasses.ElevatorSubsystemOverride;
import FaultTestClasses.FloorSubsystemOverride;
import FaultTestClasses.SchedulerOverride;
import org.junit.jupiter.api.Test;
import utill.TestConfig;

import java.io.IOException;
import java.text.ParseException;

class FaultTest {


    @Test
    void DoorFaultTest() throws IOException, ParseException {

        TestConfig config = new TestConfig();
        config.addProperty("probabilityDoorStuck", "0.9");
        FloorSubsystemOverride fs = new FloorSubsystemOverride();
        ElevatorSubsystem es = new ElevatorSubsystemOverride();
        SchedulerOverride sched = new SchedulerOverride(config);

    }
}
