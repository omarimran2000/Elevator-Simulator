package FloorSubsystem;

import model.Floors;

import java.io.IOException;

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
    Floors getWaitingPeopleUp() throws IOException, ClassNotFoundException;

    /**
     * Gets floors for people waiting down
     *
     * @return floors
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Floors getWaitingPeopleDown() throws IOException, ClassNotFoundException;

    void interrupt() throws IOException, ClassNotFoundException;
}
