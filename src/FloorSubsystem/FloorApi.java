package FloorSubsystem;

import model.SendSet;

import java.io.IOException;
import java.util.Set;

public interface FloorApi {
    SendSet getWaitingPeopleUp() throws IOException, ClassNotFoundException;

    SendSet getWaitingPeopleDown() throws IOException, ClassNotFoundException;

}
