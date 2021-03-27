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

    /**
     * Tests the door fault
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    void DoorFaultTest() throws IOException, ParseException, InterruptedException {

        TestConfig config = new TestConfig();
        //Doors will get stuck 100% of the time
        config.addProperty("probabilityDoorStuck", "100");
        //Elevator will get stuck 0% of the time
        config.addProperty("probabilityStuck","0");
        Scheduler scheduler = new Scheduler(config);

        Map<Integer, Floor> floors = FloorSubsystemOverride.generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        int maxFloor = config.getIntProperty("numFloors");
        List<Elevator> elevators = ElevatorSubsystemOverride.generateElevators(config, scheduler, maxFloor);
        scheduler.setElevators(new ArrayList<>(elevators));

        Elevator elevator = elevators.get(0);

        elevator.addDestination(new Destination(2, true));

        //Wait for elevator to actually move
        Thread.sleep(3000);
        //Check and see if the elevator doors are stuck closed
        assertFalse(elevator.getDoor().isOpen());

    }

    /**
     * Tests the floor fault
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    void FloorFaultTest() throws IOException, ParseException, InterruptedException {
        TestConfig config = new TestConfig();
        //Doors will get stuck 0% of the time
        config.addProperty("probabilityDoorStuck", "0");
        //Elevator will get stuck 100% of the time
        config.addProperty("probabilityStuck","100");
        Scheduler scheduler = new Scheduler(config);

        Map<Integer, Floor> floors = FloorSubsystemOverride.generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        int maxFloor = config.getIntProperty("numFloors");
        List<Elevator> elevators = ElevatorSubsystemOverride.generateElevators(config, scheduler, maxFloor);
        scheduler.setElevators(new ArrayList<>(elevators));

        Elevator elevator = elevators.get(0);

        elevator.addDestination(new Destination(3, true));

        //Wait for the elevators to try to move and the checkIfStuckDelay and
        //arrivalSensor to detect that it is stuck
        Thread.sleep(200001);
        //Check that the elevator is now in a stuck state
        assertEquals(ElevatorState.Stuck,elevator.getElevatorState());
    }
}
