package SchedulerSubsystem;

import model.Destination;
import model.Floors;

import java.io.IOException;

public interface SchedulerApi {
    /**
     * Used by elevator to get the floors of the waiting people up
     *
     * @param floorNumber floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Floors getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException;

    /**
     * Used by elevator to get the floors of the waiting people down
     *
     * @param floorNumber floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Floors getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException;

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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Floors getWaitingPeople(int floorNumber) throws IOException, ClassNotFoundException;

    boolean interruptThread();
}