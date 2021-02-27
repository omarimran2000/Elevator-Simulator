package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;

import java.util.*;

/**
 * The Scheduler class schedules the events
 *
 * @version Feb 27, 2021
 */
public class Scheduler implements Runnable {
    private List<Elevator> elevators;
    private Map<Integer, Floor> floors;

    /**
     * Set the list of elevators
     *
     * @param elevators The list of elevators
     */
    public void setElevators(List<Elevator> elevators) {
        if (this.elevators == null) {
            this.elevators = Collections.unmodifiableList(elevators);
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
        System.out.println("Scheduler: scheduling event for floor "+floorNumber);
        String direction = (isUp ? "Up" : "Down");
        System.out.println(direction+ " button pushed on floor " +floorNumber+ " - lamp is on");
        elevators.stream()
                .min(Comparator.comparing(elevator -> elevator.distanceTheFloor(floorNumber, isUp)))
                .orElseThrow(NoSuchElementException::new)
                .addDestination(floorNumber, isUp);
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
