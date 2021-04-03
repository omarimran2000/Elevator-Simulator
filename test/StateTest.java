import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import GUI.GUI;
import SchedulerSubsystem.Scheduler;
import model.Destination;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static FloorSubsystem.FloorSubsystem.generateFloors;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    /**
     * Testing the states
     *
     * @throws ParseException
     * @throws IOException
     */
    @Test
    void StateMethodsTest() throws ParseException, IOException, InterruptedException {

        Config config = new Config();
        Scheduler scheduler = new Scheduler(config, new GUI(config));

        Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        int maxFloor = config.getIntProperty("numFloors");
        List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, scheduler, maxFloor);
        scheduler.setElevators(new ArrayList<>(elevators));

        Elevator elevator = elevators.get(0);

        assertTrue(elevator.getIsUp()); //Can only go up from base floor
        assertFalse(elevator.getIsMoving()); //Shouldn't be moving on instantiation
        assertEquals(4, elevator.distanceTheFloor(new Destination(4, true))); //starts on floor 0 so expected is 4 instead of 3

        elevator.addDestination(new Destination(3, true));
        Thread.sleep(20000);
        assertEquals(3, elevator.getCurrentFloorNumber());
    }
}
