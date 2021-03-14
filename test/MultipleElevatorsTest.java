import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MultipleElevatorsTest {
    /**
     * Tests multiple elevators
     * @throws IOException
     */
    @Test
    public void multiElevators() throws IOException {
        Config config = new Config();
        Scheduler scheduler = new Scheduler(config);
        List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, scheduler, config.getIntProperty("numFloors"));

        //Make sure that the List is the correct size
        assertEquals(4, elevators.size());

        //Test to make sure that the actually initializes
        assertNotNull(elevators);
        scheduler.setElevators(new ArrayList<>(elevators));
        //Check that the elevators List is passed to the scheduler
        assertNotNull(scheduler.getElevators());


    }
}
