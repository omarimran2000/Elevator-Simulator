package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;

import java.time.*;
import java.util.*;


public class Scheduler implements Runnable {
    public List<Elevator> elevators;
    public Map<Integer, Floor> floors;
   // private final ScheduledExecutorService executor;
    private PriorityQueue<Event> events;
    private long timePassed;


    /**
     * Constructor for Scheduler
     */
    public Scheduler() {
        timePassed = 0;
        events = new PriorityQueue<>();

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
     * @param destinationFloor The destination floor
     * @param originalFloor the original floor
     */
    public synchronized void moveElevatorToFloorNumber(int originalFloor, int destinationFloor) {
        while(!elevators.get(0).getIdle())
        {
            try{
                wait();

            }catch(InterruptedException ex)
            {

            }
        }

        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if(currentFloor != originalFloor) {
            elevators.get(0).moveToFloorNumber(originalFloor);

        } else {
            elevators.get(0).openDoors(originalFloor);
            elevatorArrivedAtFloorNumber(originalFloor);
        }
        moveElevatorToDestination(destinationFloor);

    }
    public void moveElevatorToDestination(int destination)
    {
        System.out.println("Elevator button "+destination+" has been pressed");
        elevators.get(0).getButtons().get(destination-1).setOn(false);
        elevators.get(0).getElevatorLamps().get(destination-1).setLamp(false);
        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if(currentFloor != destination) {
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
     * @return true if there are events waiting to be run
     */
    public boolean hasEvents() {
        for(Floor f:floors.values())
        {
            if (f.hasEvents())
            {
                return true;
            }
        }
        return false;
    }

    public void closeElevatorDoors(int floor) {

            elevators.get(0).closeDoors(floor);
    }

    public void addToQueue(Event e)
    {
        events.add(e);
    }
    public void removeEvent(Event e)
    {
        events.remove(e);
    }

    public long getTimePassed() {
        return timePassed;
    }
    public Event priorityEvent(){
        return events.peek();
    }

    /**
     * The run method
     */
    @Override
    public void run() {
        Date d = new Date();
        long startTime = d.getTime();

        while(hasEvents())
        {
            d = new Date();
            timePassed = (d.getTime() - startTime)/1000;
        }
    }
}
