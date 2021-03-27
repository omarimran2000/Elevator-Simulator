import ElevatorSubsystem.*;
import FaultTestClasses.ElevatorSubsystemOverride;
import FaultTestClasses.FloorSubsystemOverride;
import FaultTestClasses.SchedulerOverride;

import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import model.Destination;
import org.junit.jupiter.api.Test;

import utill.*;

import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static FloorSubsystem.FloorSubsystem.generateFloors;
import static org.junit.jupiter.api.Assertions.*;

class FaultTest {


    @Test
    void DoorFaultTest() throws IOException, ParseException {

        TestConfig config = new TestConfig(0);
        Scheduler scheduler = new Scheduler(config);

        Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        int maxFloor = config.getIntProperty("numFloors");
        List<Elevator> elevators = ElevatorSubsystemOverride.generateElevators(config, scheduler, maxFloor);
        scheduler.setElevators(new ArrayList<>(elevators));

        Elevator elevator = elevators.get(0);

        elevator.addDestination(new Destination(3, true));
        elevator.addDestination(new Destination(12, true));
        elevator.addDestination(new Destination(14, true));
        elevator.addDestination(new Destination(18, true));
        elevator.addDestination(new Destination(21, true));
    }
}
