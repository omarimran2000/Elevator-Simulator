import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static FloorSubsystem.FloorSubsystem.generateFloors;
import static org.junit.jupiter.api.Assertions.*;

class CommunicationTest {

    /**
     * Testing the communication between systems
     *
     * @throws ParseException
     * @throws IOException
     */
    @Test
    void CommsTest() throws ParseException, IOException {
        Scheduler scheduler = new Scheduler();
        Config config = new Config();

        Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        int maxFloor = config.getIntProperty("maxFloor");
        List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, scheduler, maxFloor);
        scheduler.setElevators(elevators);

        Elevator elevator = elevators.get(0);

        assertTrue(floors.get(1).hasEvents());
        assertFalse(floors.get(0).getTop().isOn());
        assertEquals(0, elevator.getCurrentFloorNumber());

        floors.get(0).run();
        assertEquals(0, elevator.getCurrentFloorNumber());
    }
}
