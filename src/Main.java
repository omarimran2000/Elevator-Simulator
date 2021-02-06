import ElevatorSubsystem.Elevator;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static FloorSubsystem.FloorSubsystem.generateFloors;

public class Main {
    public static final String CSV_FILE_NAME = "test.csv";

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();



        try {
            Map<Integer, Floor> floors = generateFloors(scheduler, CSV_FILE_NAME);
            scheduler.setFloors(floors);
            floors.forEach((floorNumber, floor) -> new Thread(floor, "Floor " + floorNumber).start());
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        Elevator elevator = new Elevator(scheduler);
        scheduler.setElevators(List.of(elevator));
        new Thread(elevator, "Elevator 1").start();
        new Thread(scheduler, "Scheduler").start();
    }

}
