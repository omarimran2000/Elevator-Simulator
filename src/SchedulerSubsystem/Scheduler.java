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


    public Scheduler() {
        //executor = Executors.newSingleThreadScheduledExecutor();

        timePassed = 0;
        events = new PriorityQueue<>();

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

    public synchronized void elevatorArrivedAtFloorNumber(int floorNumber) {

        floors.get(floorNumber).turnButtonOff();
        closeElevatorDoors(floorNumber);

    }

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

    private boolean hasPeopleWaiting() {
        return floors.values().stream().anyMatch(Floor::hasPeopleWaiting);
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
        for (Floor f:floors.values())
        {
            if(f.getSchedule().contains(e))
            {
                f.getSchedule().remove(e);
            }
        }
    }

    public long getTimePassed() {
        return timePassed;
    }
    public Event priorityEvent(){
        return events.peek();
    }

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
