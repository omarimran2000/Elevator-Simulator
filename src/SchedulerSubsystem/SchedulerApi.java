package SchedulerSubsystem;

import model.Destination;
import model.SendSet;

import java.io.IOException;

public interface SchedulerApi {
    SendSet getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException;

    SendSet getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException;

    void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException;

}