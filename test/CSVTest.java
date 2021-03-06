import FloorSubsystem.FloorSubsystem;
import model.Event;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CSVTest {
    Config config = new Config();

    CSVTest() throws IOException {
    }

    /**
     * Testing the reading of the input csv file
     *
     * @throws FileNotFoundException
     * @throws ParseException
     */
    @Test
    void readCSVTest() throws FileNotFoundException, ParseException {
        List<Event> list = FloorSubsystem.readCSV(config, config.getProperty("csvFileName"));
        assertNotNull(list);
        System.out.print(list.get(0));
    }
}