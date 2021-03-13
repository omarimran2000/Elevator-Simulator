package FloorSubsystem;

import model.SendSet;

import java.io.IOException;

public interface FloorApi {
    SendSet getWaitingPeopleUp() throws IOException, ClassNotFoundException;

    SendSet getWaitingPeopleDown() throws IOException, ClassNotFoundException;

}
