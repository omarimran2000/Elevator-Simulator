import ElevatorSubsystem.ElevatorSubsystem;
import FaultTestClasses.ElevatorSubsystemOverride;
import FaultTestClasses.FloorSubsystemOverride;
import FaultTestClasses.SchedulerOverride;

import org.junit.jupiter.api.Test;

import utill.*;

import java.io.IOException;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class FaultTest {


    @Test
    void DoorFaultTest() throws IOException, ParseException {

        TestConfig config = new TestConfig(0);
        FloorSubsystemOverride fs = new FloorSubsystemOverride();
        ElevatorSubsystem es = new ElevatorSubsystemOverride();
        SchedulerOverride sched = new SchedulerOverride(config);

    }
}
