package SchedulerSubsystem;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;

import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler implements Runnable {
    public List<Elevator> elevators;
    public Map<Integer, Floor> floors;
   // private final ScheduledExecutorService executor;
    private PriorityQueue<Event> events;
    public Timer timer;


    public Scheduler() {
        //executor = Executors.newSingleThreadScheduledExecutor();
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

    public void moveElevatorToFloorNumber(int floorNumber) {


        int currentFloor = elevators.get(0).getCurrentFloorNumber();
        if(currentFloor != floorNumber) {
            elevators.get(0).moveToFloorNumber(floorNumber);
        } else {
            elevators.get(0).openDoors(floorNumber);
            elevatorArrivedAtFloorNumber(floorNumber);
        }


    }

    public void elevatorArrivedAtFloorNumber(int floorNumber) {

        /*
        executor.schedule(() -> {
            closeElevatorDoors();
            if (floors.get(floorNumber).hasPeopleWaiting()) {

                moveElevatorToFloorNumber(floors.get(floorNumber).getNextElevatorButton());
            }
        }, 1, TimeUnit.SECONDS); // fix delay

         */
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
    @Override
    public void run() {
        while(hasEvents())
        {

        }
    }
}
