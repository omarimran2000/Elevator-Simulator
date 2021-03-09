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
        scheduler.setFloors(floors);

        int maxFloor = config.getIntProperty("maxFloor");
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(config,scheduler,maxFloor);
        scheduler.setElevators(elevatorSubsystem);

        Elevator elevator = elevatorSubsystem.getElevators().get(0);

        assertTrue(floors.get(1).hasEvents());
        assertFalse(floors.get(0).getTop().isOn());
        assertEquals(0, elevator.getCurrentFloorNumber());

        floors.get(0).run();
        assertEquals(0, elevator.getCurrentFloorNumber());
    }
}
