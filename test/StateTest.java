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

class StateTest {

    @Test
    void StateMethodsTest() throws ParseException {
        Scheduler scheduler = new Scheduler();

        List<Event> events = new ArrayList<>();
        events.add(new Event(CSV_DATE_FORMAT.parse("01-01-2021 14:00:05"), 1, true, 2));

        Map<Integer, Floor> floors = FloorSubsystem.generateFloors(scheduler, events);
        scheduler.setFloors(floors);

        Elevator elevator = new Elevator(1, scheduler, floors.size());
        scheduler.setElevators(List.of(elevator));

        assertEquals(4,elevator.distanceTheFloor(4,true)); //starts on floor 0 so expected is 4 instead of 3
        elevator.addDestination(3,true);
        Object[] futureFloors = elevator.getDestinationPath().toArray();

        System.out.println(futureFloors[0].getClass().toString());

        floors.get(0).run();
    }
}
