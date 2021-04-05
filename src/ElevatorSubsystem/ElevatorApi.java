package ElevatorSubsystem;

import model.Destination;

import java.io.IOException;

public interface ElevatorApi {
    /**
     * Returns the distance to the floor given the current destination of the elevator and the elevators current position
     *
     * @param destination
     * @return distance between elevator and its destination
     * @throws IOException
     * @throws ClassNotFoundException
     */
    int distanceTheFloor(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Adds a desination for the elevator
     *
     * @param destination
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void addDestination(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Checks if the elevator can add the desired destination to its queue list
     *
     * @param destination
     * @return a boolean value that represents whether or not the destination can be added to its queue list
     * @throws IOException
     * @throws ClassNotFoundException
     */
    boolean canAddDestination(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Interrupt method for the Elevator threads
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void interrupt() throws IOException, ClassNotFoundException;
}
