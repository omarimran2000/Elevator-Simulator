package FloorSubsystem;

import java.io.IOException;
import java.util.HashSet;

/**
 * API for the floor
 *
 * @version April 4, 2021
 */
public interface FloorApi {

    /**
     * Gets the waiting people
     *
     * @param isUp the direction
     * @return the set of floor numbers
     * @throws IOException
     * @throws ClassNotFoundException
     */
    HashSet<Integer> getWaitingPeople(boolean isUp) throws IOException, ClassNotFoundException;

    /**
     * Interrupt method
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void interrupt() throws IOException, ClassNotFoundException;

}
