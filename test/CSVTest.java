import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Event;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CSVTest {

    @Test
    void readCSVTest() throws FileNotFoundException, ParseException {
        List<Event> list = FloorSubsystem.readCSV("test.csv");
        assertNotNull(list);
        System.out.print(list.get(0));
    }
}