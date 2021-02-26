import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Event;
import org.junit.jupiter.api.Test;
import util.Config;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class CSVTest {

    @Test
    void readCSVTest() {
        try {
            Config config = new Config();
            List<Event> list = FloorSubsystem.readCSV(config, "test.csv");
            assertNotNull(list);
            System.out.print(list.get(0));
        } catch (IOException | ParseException e) {
            fail();
        }
    }
}