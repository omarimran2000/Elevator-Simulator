import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static FloorSubsystem.FloorSubsystem.generateFloors;
import static org.junit.jupiter.api.Assertions.*;

class FloorsTest {

    @Test
    void FloorsCreationTest() throws ParseException, IOException {
        Scheduler scheduler = new Scheduler();
        Config config = new Config();

        Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));

        assertNotNull(floors);
        assertEquals(5, floors.size());

        assertNull(floors.get(0).getBottom());
        assertNull(floors.get(4).getTop());

        assertNotNull(floors.get(1).getTop());
        assertNotNull(floors.get(1).getBottom());

        assertNotNull(floors.get(2).getTop());
        assertNotNull(floors.get(2).getBottom());

        assertNotNull(floors.get(3).getTop());
        assertNotNull(floors.get(3).getBottom());
    }
}
