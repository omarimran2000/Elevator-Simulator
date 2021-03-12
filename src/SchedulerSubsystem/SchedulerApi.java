package SchedulerSubsystem;

import java.io.IOException;
import java.util.Set;

public interface SchedulerApi {
    Set<Integer> getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException;

    Set<Integer> getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException;

    void handleFloorButton(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException;

}