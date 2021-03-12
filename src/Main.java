import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import utill.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static FloorSubsystem.FloorSubsystem.generateFloors;
import static ElevatorSubsystem.ElevatorSubsystem.generateElevators;

/**
 * This is the main class that starts the threads
 *
 * @version Feb 27, 2021
 */
public class Main {

    /**
     * Initializes the map of floors, the scheduler and the elevator
     * Starts the threads
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Scheduler scheduler = new Scheduler();
        try {
            Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));
          
            scheduler.setFloors(floors);
            int maxFloor = config.getIntProperty("maxFloor");
            List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, scheduler, maxFloor);
            scheduler.setElevators(elevators);
            floors.forEach((floorNumber, floor) -> new Thread(floor, "Floor " + floorNumber).start());
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        new Thread(scheduler, "Scheduler").start();
    }
}
