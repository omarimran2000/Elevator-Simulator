package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler implements Runnable {
    private List<Elevator> elevators;
    private Map<Integer, Floor> floors;
    private final ScheduledExecutorService executor;


    /**
     * Constructor for Scheduler
     */
    public Scheduler() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Getter for the list of all the elevators in the system
     *
     * @return The list of elevators
     */
    public List<Elevator> getElevators() {
        return elevators;
    }

    /**
     * Set the list of elevators
     * @param elevators The list of elevators
     */
    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    /**
     * @return The map of floors
     */
    public Map<Integer, Floor> getFloors() {
        return floors;
    }

    /**
     * Set the map of floors in the system
     * @param floors The map of floors in the system
     */
    public void setFloors(Map<Integer, Floor> floors) {
        this.floors = floors;
    }

    /**
     * Moves the elevator car to the requested floor
     * @param floorNumber The destination floor
     */
    public void moveElevatorToFloorNumber(int floorNumber) {
        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if (currentFloor != floorNumber) {
            elevators.get(0).moveToFloorNumber(floorNumber);
        } else {
            elevators.get(0).openDoors();
            elevatorArrivedAtFloorNumber(floorNumber);
        }
    }

    /**
     * @param floorNumber The floor number
     */
    public void elevatorArrivedAtFloorNumber(int floorNumber) {
        executor.schedule(() -> {
            closeElevatorDoors();
            if (floors.get(floorNumber).hasPeopleWaiting()) {

                moveElevatorToFloorNumber(floors.get(floorNumber).getNextElevatorButton());
            }
        }, 1, TimeUnit.SECONDS); // fix delay
    }

    /**
     * @return true if there are events waiting to be run
     */
    private boolean hasEvents() {
        return floors.values().stream().anyMatch(Floor::hasEvents);
    }

    /**
     * @return true if there are people waiting for an elevator
     */
    private boolean hasPeopleWaiting() {
        return floors.values().stream().anyMatch(Floor::hasPeopleWaiting);
    }

    /**
     * @return true if there is an elevator moving in the system
     */
    private boolean hasMovingElevator() {
        return elevators.stream().anyMatch(Elevator::isMoving);
    }

    /**
     * Shutdown the threads
     */
    public void shutdown() {
        if (!hasEvents() && !hasPeopleWaiting() && !hasMovingElevator()) {
            elevators.forEach(Elevator::shutdown);
            floors.forEach((k, v) -> v.shutdown());
            executor.shutdown();
        }
    }

    /**
     * Opens the elevator doors
     */
    public void openElevatorDoors() {
        elevators.get(0).openDoors();
    }

    /**
     * Closes the elevator doors
     */
    public void closeElevatorDoors() {
        elevators.get(0).closeDoors();
    }


    /**
     * The run method
     */
    @Override
    public void run() {
    }
}
