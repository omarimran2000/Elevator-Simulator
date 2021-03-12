package ElevatorSubsystem;

import java.io.IOException;

public interface ElevatorApi {
    int distanceTheFloor(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException;

    void addDestination(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException;
}
