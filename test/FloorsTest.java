import FloorSubsystem.Floor;
import GUI.GUI;
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

        Config config = new Config();
        GUI gui = new GUI(config,null,null,null);
        Scheduler scheduler = new Scheduler(config, gui);

        Map<Integer, Floor> floors = generateFloors(config, scheduler, gui, config.getProperty("csvFileName"));

        assertNotNull(floors);
        assertEquals(23, floors.size());

        assertNull(floors.get(0).getBottom());
        assertNull(floors.get(22).getTop());

        assertNotNull(floors.get(1).getTop());
        assertNotNull(floors.get(1).getBottom());

        assertNotNull(floors.get(2).getTop());
        assertNotNull(floors.get(2).getBottom());

        assertNotNull(floors.get(3).getTop());
        assertNotNull(floors.get(3).getBottom());
    }
}
