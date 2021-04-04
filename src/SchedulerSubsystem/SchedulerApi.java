package SchedulerSubsystem;

import model.Destination;

import java.io.IOException;
import java.util.HashSet;

public interface SchedulerApi {
    /**
     * Used by elevator to get the floors of the waiting people up
     *
     * @param destination floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    HashSet<Integer> getWaitingPeople(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Used by the floor to handle floor button
     *
     * @param destination the destination
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Get waiting people
     *
     * @param floorNumber the destination
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    HashSet<Destination> getUnscheduledPeople(int floorNumber) throws IOException, ClassNotFoundException;

    void interrupt() throws IOException, ClassNotFoundException;
}