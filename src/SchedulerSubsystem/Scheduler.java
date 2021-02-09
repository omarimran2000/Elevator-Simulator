package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;

import java.util.List;
import java.util.Map;

/**
 * The Scheduler class schedules the events
 *
 * @version Feb 06, 2021
 */
public class Scheduler implements Runnable {
    public List<Elevator> elevators;
    public Map<Integer, Floor> floors;


    /**
     * Constructor for Scheduler
     */
    public Scheduler() {
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
     *
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
     *
     * @param floors The map of floors in the system
     */
    public void setFloors(Map<Integer, Floor> floors) {
        this.floors = floors;
    }

    /**
     * Moves the elevator car to the requested floor
     *
     * @param destinationFloor The destination floor
     * @param originalFloor    the original floor
     */
    public synchronized void moveElevatorToFloorNumber(int originalFloor, int destinationFloor) {
        while (!elevators.get(0).getIdle()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if (currentFloor != originalFloor) {
            elevators.get(0).moveToFloorNumber(originalFloor);

        } else {
            elevators.get(0).openDoors(originalFloor);
            elevatorArrivedAtFloorNumber(originalFloor);
        }
        moveElevatorToDestination(destinationFloor);

    }

    /**
     * Moving an elevator to the destination
     *
     * @param destination the destination to move it
     */
    public void moveElevatorToDestination(int destination) {
        System.out.println("Elevator button " + destination + " has been pressed");
        elevators.get(0).getButtons().get(destination).setOn(false);
        elevators.get(0).getElevatorLamps().get(destination).setLamp(false);
        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if (currentFloor != destination) {
            elevators.get(0).moveToFloorNumber(destination);
        } else {
            elevators.get(0).openDoors(destination);
            elevatorArrivedAtFloorNumber(destination);
        }
        elevators.get(0).setIdle(true);
        notifyAll();
    }

    /**
     * @param floorNumber The floor number
     */

    public synchronized void elevatorArrivedAtFloorNumber(int floorNumber) {

        floors.get(floorNumber).turnButtonOff();
        closeElevatorDoors(floorNumber);

    }


    /**
     * @param floor The Floor Number
     */

    public void closeElevatorDoors(int floor) {

        elevators.get(0).closeDoors(floor);
    }

    /**
     * This is useless but demanded by the manual.
     */
    @Override
    public void run() {

    }
}
