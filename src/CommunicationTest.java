import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationTest {

    @Test
    void CommsTest() throws FileNotFoundException, ParseException {
        Scheduler scheduler = new Scheduler();
        FloorSubsystem floorSubsystem = new FloorSubsystem();

        Map<Integer, Floor> floors = floorSubsystem.generateFloors(scheduler, "test.csv");
        scheduler.setFloors(floors);

        Elevator elevator = new Elevator(scheduler);
        scheduler.setElevators(List.of(elevator));

        assertTrue(scheduler.hasEvents());
        assertEquals(0,elevator.getCurrentFloorNumber());
        scheduler.moveElevatorToFloorNumber(scheduler.getEvents().poll().getFloor());
        assertEquals(1,elevator.getCurrentFloorNumber());
    }
}
