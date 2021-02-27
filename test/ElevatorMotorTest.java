import ElevatorSubsystem.Motor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ElevatorMotorTest {

    /**
     * Testing the initialization of the motor
     */
    @Test
    public void motorTest() {
        Motor motor = new Motor();

        //Default setting for directionIsUp is False
        assertTrue(motor.directionIsUp());
        motor.setDirectionIsUp(false);
        assertFalse(motor.directionIsUp());

        //Default setting for isMoving is False
        assertFalse(motor.isMoving());
        motor.setMoving(true);
        assertTrue(motor.isMoving());
    }
}