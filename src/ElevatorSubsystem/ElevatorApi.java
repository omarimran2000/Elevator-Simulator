package ElevatorSubsystem;

import model.Destination;

import java.io.IOException;

public interface ElevatorApi {
    int distanceTheFloor(Destination destination) throws IOException, ClassNotFoundException;

    void addDestination(Destination destination) throws IOException, ClassNotFoundException;

    boolean canAddDestination(Destination destination) throws IOException, ClassNotFoundException;
}
