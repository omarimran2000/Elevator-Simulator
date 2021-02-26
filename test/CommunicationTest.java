import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static FloorSubsystem.FloorSubsystem.CSV_DATE_FORMAT;
import static org.junit.jupiter.api.Assertions.*;

class CommunicationTest {

    @Test
    void CommsTest() throws ParseException {
        Scheduler scheduler = new Scheduler();

        List<Event> events = new ArrayList<>();
        events.add(new Event(CSV_DATE_FORMAT.parse("01-01-2021 14:00:05"), 1, true, 2));

        Map<Integer, Floor> floors = FloorSubsystem.generateFloors(scheduler, events);
        scheduler.setFloors(floors);

        Elevator elevator = new Elevator(1, scheduler, floors.size());
        scheduler.setElevators(List.of(elevator));

        assertTrue(floors.get(1).hasEvents());
        assertFalse(floors.get(0).getTop().isOn());
        assertEquals(0, elevator.getCurrentFloorNumber());

        floors.get(0).run();
        assertEquals(1, elevator.getCurrentFloorNumber());
    }
}
