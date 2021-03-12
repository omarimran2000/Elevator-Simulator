package FloorSubsystem;

import java.io.IOException;
import java.util.Set;

public interface FloorApi {
    Set<Integer> getWaitingPeopleUp() throws IOException, ClassNotFoundException;

    Set<Integer> getWaitingPeopleDown() throws IOException, ClassNotFoundException;

}
