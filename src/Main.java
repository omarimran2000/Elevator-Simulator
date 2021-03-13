import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import utill.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static FloorSubsystem.FloorSubsystem.generateFloors;

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
        Scheduler scheduler = new Scheduler(config);
        try {
            Map<Integer, Floor> floors = generateFloors(config, scheduler, config.getProperty("csvFileName"));

            scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, scheduler, Collections.max(floors.keySet()));
            scheduler.setElevators(new ArrayList<>(elevators));
            floors.forEach((floorNumber, floor) -> new Thread(floor, "Floor " + floorNumber).start());
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        new Thread(scheduler, "Scheduler").start();
    }
}
