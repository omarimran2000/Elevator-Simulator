import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
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

    /**
     * Testing the states
     *
     * @throws ParseException
     * @throws IOException
     */
    @Test
    void StateMethodsTest() throws ParseException, IOException, InterruptedException {
        Scheduler scheduler = new Scheduler();
        Config config = new Config();

        Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));
        scheduler.setFloors(floors);

        int maxFloor = config.getIntProperty("maxFloor");
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(config,scheduler,maxFloor);
        scheduler.setElevators(elevatorSubsystem);

        Elevator elevator = elevatorSubsystem.getElevators().get(0);

        assertTrue(elevator.getIsUp()); //Can only go up from base floor
        assertFalse(elevator.getIsMoving()); //Shouldn't be moving on instantiation
        assertEquals(4, elevator.distanceTheFloor(4, true)); //starts on floor 0 so expected is 4 instead of 3

        elevator.addDestination(3, true);
        Thread.sleep(20000);
        assertEquals(3, elevator.getCurrentFloorNumber());
    }
}
