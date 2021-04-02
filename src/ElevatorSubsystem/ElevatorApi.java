package ElevatorSubsystem;

import model.Destination;

import java.io.IOException;


public interface ElevatorApi {
    /**
     * Get the number of floors between the current and destination floors.
     *
     * @param destination The potential destination for the Elevator.
     * @return the distance between the two floors.
     * @throws IOException            IOException is thrown if the server fails to send or receive to the port.
     * @throws ClassNotFoundException ClassNotFoundException is thrown if serialization failed.
     */
    int distanceTheFloor(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Add the specified destination to the list of destinations.
     *
     * @param destination The new destination for the Elevator.
     * @throws IOException            IOException is thrown if the server fails to send or receive to the port.
     * @throws ClassNotFoundException ClassNotFoundException is thrown if serialization failed.
     */
    void addDestination(Destination destination) throws IOException, ClassNotFoundException;

    /**
     * Get if a destination is in the current path of the Elevator.
     * If idleDestination is set and the new destination is further away than the idleDestination, the Elevator will not accept the new destination because it can not guaranty that will not turn around at the idleDestination.
     *
     * @param destination The potential destination for the Elevator.
     * @return If the destination is in the current path of the Elevator.
     * @throws IOException            IOException is thrown if the server fails to send or receive to the port.
     * @throws ClassNotFoundException ClassNotFoundException is thrown if serialization failed.
     */
    boolean canAddDestination(Destination destination) throws IOException, ClassNotFoundException;
}
