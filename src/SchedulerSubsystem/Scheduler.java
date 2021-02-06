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

    /**
     * Getter for the list of all the elevators in the system
     * @return The list of elevators
     */
    public List<Elevator> getElevators() {
        return elevators;
    }

    /**
     *
     */
    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    /**
     *
     * @return
     */
    public Map<Integer, Floor> getFloors() {
        return floors;
    }

    /**
     *
     * @param floors
     */
    public void setFloors(Map<Integer, Floor> floors) {
        this.floors = floors;
    }

    /**
     *
     * @param floorNumber
     */
    public void moveElevatorToFloorNumber(int floorNumber) {
        elevators.get(0).moveToFloorNumber(floorNumber);
    }

    /**
     *
     * @param floorNumber The floor number
     */
    public void elevatorArrivedAtFloorNumber(int floorNumber) {
        if (floors.get(floorNumber).hasPeopleWaiting()) {
            moveElevatorToFloorNumber(floors.get(floorNumber).getNextElevatorButton());
        }
    }

    /**
     * @return The number of events left
     */
    public int getNumEvents()
    {
        int count = 0;
        for (int index:floors.keySet())
        {
            count+=floors.get(index).getNumEvents();
        }
        return count;
    }

    /**
     *
     */
    @Override
    public void run() {
        while(getNumEvents()>0)
        {

        }
    }
}
