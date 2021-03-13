package SchedulerSubsystem;

import model.SendSet;

import java.io.IOException;

public interface SchedulerApi {
    SendSet getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException;

    SendSet getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException;

    void handleFloorButton(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException;

}