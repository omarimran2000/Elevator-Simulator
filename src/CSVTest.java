import SchedulerSubsystem.*;
import FloorSubsystem.FloorSubsystem;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVTest {

    @Test
    void readCSVTest() throws FileNotFoundException, ParseException {
        FloorSubsystem floorSubsystem = new FloorSubsystem();
        List<Event> list = floorSubsystem.readCSV("test.csv");
        assertNotNull(list);
        System.out.print(list.get(0));
    }
}