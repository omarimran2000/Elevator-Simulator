package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;

import java.util.List;
import java.util.Map;

public class Scheduler implements Runnable {
    public List<Elevator> elevators;
    public Map<Integer, Floor> floors;

    public Scheduler() {

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
        elevators.get(0).moveToFloorNumber(floorNumber);
    }

    public void elevatorArrivedAtFloorNumber(int floorNumber) {
        if (floors.get(floorNumber).hasPeopleWaiting()) {
            moveElevatorToFloorNumber(floors.get(floorNumber).getNextElevatorButton());
        }
    }

    @Override
    public void run() {

    }
}
