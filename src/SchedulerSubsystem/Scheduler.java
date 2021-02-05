package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler implements Runnable {
    public List<Elevator> elevators;
    public Map<Integer, Floor> floors;
    private final ScheduledExecutorService executor;


    public Scheduler() {
        executor = Executors.newSingleThreadScheduledExecutor();

    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public Map<Integer, Floor> getFloors() {
        return floors;
    }

    public void setFloors(Map<Integer, Floor> floors) {
        this.floors = floors;
    }

    public void moveElevatorToFloorNumber(int floorNumber) {


        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if(currentFloor != floorNumber) {
            elevators.get(0).moveToFloorNumber(floorNumber);
        } else {
            elevators.get(0).openDoors();
            elevatorArrivedAtFloorNumber(floorNumber);
        }


    }

    public void elevatorArrivedAtFloorNumber(int floorNumber) {


        executor.schedule(() -> {
            closeElevatorDoors();
            if (floors.get(floorNumber).hasPeopleWaiting()) {

                moveElevatorToFloorNumber(floors.get(floorNumber).getNextElevatorButton());
            }
        }, 1, TimeUnit.SECONDS); // fix delay
    }

    private boolean hasEvents() {
        return floors.values().stream().anyMatch(Floor::hasEvents);
    }

    private boolean hasPeopleWaiting() {
        return floors.values().stream().anyMatch(Floor::hasPeopleWaiting);
    }

    private boolean hasMovingElevator() {
        return elevators.stream().anyMatch(Elevator::isMoving);
    }

    public void shutdown() {
        if (!hasEvents() && !hasPeopleWaiting() && !hasMovingElevator()) {
            //elevators.forEach(Elevator::shutdown);
            //floors.forEach((k, v) -> v.shutdown());
            //executor.shutdown();
            System.exit(0);
        }
    }

    public void openElevatorDoors(){
        elevators.get(0).openDoors();
    }

    public void closeElevatorDoors() {

            elevators.get(0).closeDoors();
    }


    @Override
    public void run() {
    }
}
