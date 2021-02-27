import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import utill.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static FloorSubsystem.FloorSubsystem.generateFloors;

/**
 * This is the main class that starts the threads
 *
 * @version Feb 06, 2021
 */
public class Main {

    /**
     * Initializes the map of floors, the shceduler and the elevator
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
            Elevator elevator = new Elevator(config, scheduler, 1, floors.keySet().stream().max(Comparator.comparingInt(k -> k)).orElseThrow());
            scheduler.setElevators(List.of(elevator));
            floors.forEach((floorNumber, floor) -> new Thread(floor, "Floor " + floorNumber).start());
            new Thread(elevator, "Elevator 1").start();
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        new Thread(scheduler, "Scheduler").start();
    }

}
