package SchedulerSubsystem;

import model.Destination;
import model.Floors;

import java.io.IOException;

public interface SchedulerApi {
    Floors getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException;

    Floors getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException;

    void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException;

    Floors getWaitingPeople(int floorNumber) throws IOException, ClassNotFoundException;
}