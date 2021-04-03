import ElevatorSubsystem.Motor;
import GUI.GUI;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorMotorTest {

    /**
     * Testing the initialization of the motor
     */
    @Test
    public void motorTest() throws IOException {
        Motor motor = new Motor(0, new GUI(new Config()));

        //Default setting for directionIsUp is False
        assertTrue(motor.directionIsUp());
        try {
            motor.setDirectionIsUp(false);
        } catch (ClassNotFoundException e) {
            fail();
        }
        assertFalse(motor.directionIsUp());

        //Default setting for isMoving is false
        assertFalse(motor.isMoving());
        motor.setMoving(true);
        assertTrue(motor.isMoving());
    }
}