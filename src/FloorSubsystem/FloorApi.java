package FloorSubsystem;

import model.Floors;

import java.io.IOException;

public interface FloorApi {
    Floors getWaitingPeopleUp() throws IOException, ClassNotFoundException;

    Floors getWaitingPeopleDown() throws IOException, ClassNotFoundException;

}
