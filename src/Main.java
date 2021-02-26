import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import util.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static FloorSubsystem.FloorSubsystem.generateFloors;

/**
 * This is the main class that starts the threads
 *
 * @version Feb 06, 2021
 */
public class Main {
    public static final String CSV_FILE_NAME = "test.csv";

    /**
     * Initializes the map of floors, the shceduler and the elevator
     * Starts the threads
     *
     * @param args
     * @throws IOException If it fails to parse the config file.
     */
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Scheduler scheduler = new Scheduler();
        try {
            Map<Integer, Floor> floors = generateFloors(config, scheduler, CSV_FILE_NAME);
            scheduler.setFloors(floors);
            floors.forEach((floorNumber, floor) -> new Thread(floor, "Floor " + floorNumber).start());
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        Elevator elevator = new Elevator(scheduler, config);
        scheduler.setElevators(List.of(elevator));
        new Thread(elevator, "Elevator 1").start();
        new Thread(scheduler, "Scheduler").start();
    }
}
