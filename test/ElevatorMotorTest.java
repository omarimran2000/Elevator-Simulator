import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.Motor;
import SchedulerSubsystem.Scheduler;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    @Test
    public void motorTest() {
        Motor motor = new Motor();

        //Default setting for directionIsUp is False
        assertTrue(motor.directionIsUp());
        motor.setDirectionIsUp(false);
        assertFalse(motor.directionIsUp());

        //Default setting for isMoving is True
        assertTrue(motor.isMoving());
        motor.setMoving(false);
        assertFalse(motor.isMoving());
    }
}