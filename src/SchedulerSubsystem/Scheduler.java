package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;

import java.util.*;

/**
 * The Scheduler class schedules the events
 *
 * @version Feb 27, 2021
 */
public class Scheduler implements Runnable {
    private Map<Integer, Floor> floors;
    private ElevatorSubsystem elevatorSubsystem;

    /**
     * Set the list of elevators
     *
     * @param elevatorSubsystem The elevator subsystem
     */
    public void setElevators(ElevatorSubsystem elevatorSubsystem) {
        if (this.elevatorSubsystem == null) {
            this.elevatorSubsystem = elevatorSubsystem;
        }
    }

    /**
     * Set the map of floors in the system
     *
     * @param floors The map of floors in the system
     */
    public void setFloors(Map<Integer, Floor> floors) {
        if (this.floors == null) {
            this.floors = Collections.unmodifiableMap(floors);
        }
    }

    public void handleFloorButton(int floorNumber, boolean isUp) {
        System.out.println("Scheduler: scheduling event for floor " + floorNumber);
        Elevator best = elevatorSubsystem.getBestElevator(floorNumber, isUp);
        best.addDestination(floorNumber, isUp);
    }


    /**
     * The run method
     */
    @Override
    public void run() {
    }

    public Set<Integer> getWaitingPeopleUp(int floorNumber) {
        return floors.get(floorNumber).getWaitingPeopleUp();
    }

    public Set<Integer> getWaitingPeopleDown(int floorNumber) {
        return floors.get(floorNumber).getWaitingPeopleDown();
    }
}
