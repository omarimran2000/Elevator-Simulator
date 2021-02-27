import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


import static FloorSubsystem.FloorSubsystem.generateFloors;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void StateMethodsTest() throws ParseException, IOException {
        Scheduler scheduler = new Scheduler();
        Config config = new Config();

        Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors);
        Elevator elevator = new Elevator(config, scheduler, 1, floors.keySet().stream().max(Comparator.comparingInt(k -> k)).orElseThrow());
        scheduler.setElevators(List.of(elevator));

        assertTrue(elevator.getIsUp()); //Can only go up from base floor
        assertFalse(elevator.getIsMoving()); //Shouldn't be moving on instantiation
        assertEquals(4,elevator.distanceTheFloor(4,true)); //starts on floor 0 so expected is 4 instead of 3

        elevator.addDestination(3,true);
        Object[] futureFloors = elevator.getDestinationPath().toArray();

        assertEquals(3,futureFloors[0]); //The floor number on the queue is floor 3
    }
}
