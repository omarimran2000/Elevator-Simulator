import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CommunicationTest {

    @Test
    void CommsTest() {
        Scheduler scheduler = new Scheduler();

        List<Event> events = new ArrayList<>();
        events.add(new Event(145, 1, true, 2));

        Map<Integer, Floor> floors = FloorSubsystem.generateFloors(scheduler, events);
        scheduler.setFloors(floors);

        Elevator elevator = new Elevator(scheduler);
        scheduler.setElevators(List.of(elevator));

        assertFalse(floors.get(0).getTop().isOn());
        assertEquals(0, elevator.getCurrentFloorNumber());

        floors.get(0).moveElevator(1);
        assertEquals(1, elevator.getCurrentFloorNumber());
    }
}
