import org.junit.jupiter.api.Test;
import utill.Config;
import package ElevatorSubSystem;
import package SchedulerSubsystem;
import static org.junit.jupiter.api.Assertions.*;

public class MultipleElevatorsTest {

    @Test
    public void multiElevators() {
        Scheduler scheduler = new Scheduler();
        List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, scheduler, Collections.max(floors.keySet()));

        //Make sure that the List is the correct size
        assertEquals(4,elevators.size);

        //Test to make sure that the actually initializes
        assertNotNull(elevators);
        scheduler.setElevators(new ArrayList<>(elevators));
        //Check that the elevators List is passed to the scheduler
        assertNotNull(scheduler.getElevators());


    }
}
