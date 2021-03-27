import ElevatorSubsystem.*;
import FaultTestClasses.ElevatorSubsystemOverride;
import FaultTestClasses.FloorSubsystemOverride;
import FloorSubsystem.*;
import SchedulerSubsystem.Scheduler;
import model.Destination;
import model.ElevatorState;
import org.junit.jupiter.api.Test;

import utill.*;

import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FaultTest {


    @Test
    void DoorFaultTest() throws IOException, ParseException, InterruptedException {

        TestConfig config = new TestConfig();
        config.addProperty("probabilityDoorStuck", "100");
        Scheduler scheduler = new Scheduler(config);

        Map<Integer, Floor> floors = FloorSubsystemOverride.generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        int maxFloor = config.getIntProperty("numFloors");
        List<Elevator> elevators = ElevatorSubsystemOverride.generateElevators(config, scheduler, maxFloor);
        scheduler.setElevators(new ArrayList<>(elevators));

        Elevator elevator = elevators.get(0);

        elevator.addDestination(new Destination(1, true));

        Thread.sleep(3000);
        assertFalse(elevator.getDoor().isOpen());
        assertEquals(ElevatorState.Stuck,elevator.getElevatorState());
    }
}
