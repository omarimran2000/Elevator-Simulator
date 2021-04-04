package FloorSubsystem;

import java.io.IOException;
import java.util.HashSet;

/**
 * API for the floor
 */
public interface FloorApi {

    HashSet<Integer> getWaitingPeople(boolean isUp) throws IOException, ClassNotFoundException;

    void interrupt() throws IOException, ClassNotFoundException;

}
