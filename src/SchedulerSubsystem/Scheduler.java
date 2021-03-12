package SchedulerSubsystem;


import ElevatorSubsystem.ElevatorApi;
import FloorSubsystem.FloorApi;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.logging.Logger;

/**
 * The Scheduler class schedules the events
 *
 * @version Feb 27, 2021
 */
public class Scheduler implements Runnable, SchedulerApi {
    private final Logger logger;
    private List<ElevatorApi> elevators;
    private Map<Integer, FloorApi> floors;

    public Scheduler() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Set the list of elevators
     *
     * @param elevators The elevators
     */
    public void setElevators(List<ElevatorApi> elevators) {
        if (this.elevators == null) {
            this.elevators = Collections.unmodifiableList(elevators);
        }
    }

    /**
     * Set the map of floors in the system
     *
     * @param floors The map of floors in the system
     */
    public void setFloors(Map<Integer, FloorApi> floors) {
        if (this.floors == null) {
            this.floors = Collections.unmodifiableMap(floors);
        }
    }

    public void handleFloorButton(int floorNumber, boolean isUp) throws IOException, ClassNotFoundException {
        logger.info("Scheduler: scheduling event for floor " + floorNumber);
        elevators.stream()
                .min(Comparator.comparing(elevator -> {
                    try {
                        return elevator.distanceTheFloor(floorNumber, isUp);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new UndeclaredThrowableException(e);
                    }
                }))
                .orElseThrow(NoSuchElementException::new)
                .addDestination(floorNumber, isUp);
    }


    /**
     * The run method
     */
    @Override
    public void run() {
    }

    public Set<Integer> getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException {
        return floors.get(floorNumber).getWaitingPeopleUp();
    }

    public Set<Integer> getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException {
        return floors.get(floorNumber).getWaitingPeopleDown();
    }
}
