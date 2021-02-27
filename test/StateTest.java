import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;
import util.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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

        Elevator elevator = new Elevator(config,scheduler,1, floors.size());
        scheduler.setElevators(List.of(elevator));
        assertTrue(elevator.getIsUp());
        assertFalse(elevator.getIsMoving());

        assertEquals(4,elevator.distanceTheFloor(4,true)); //starts on floor 0 so expected is 4 instead of 3
        elevator.addDestination(3,true);
        Object[] futureFloors = elevator.getDestinationPath().toArray();

        assertEquals(3,futureFloors[0]);


        floors.get(0).run();
    }
}
