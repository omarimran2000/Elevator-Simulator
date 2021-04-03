package FloorSubsystem;

import java.io.IOException;
import java.util.HashSet;

/**
 * API for the floor
 */
public interface FloorApi {
    /**
     * Gets floors for people waiting up
     *
     * @return floors
     * @throws IOException
     * @throws ClassNotFoundException
     */
    HashSet<Integer> getWaitingPeopleUp() throws IOException, ClassNotFoundException;

    /**
     * Gets floors for people waiting down
     *
     * @return floors
     * @throws IOException
     * @throws ClassNotFoundException
     */
    HashSet<Integer> getWaitingPeopleDown() throws IOException, ClassNotFoundException;

    void interrupt() throws IOException, ClassNotFoundException;
}
