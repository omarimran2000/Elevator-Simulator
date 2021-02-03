import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Scheduler;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class Main {
    public static final String CSV_FILE_NAME = "test.csv";

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        Thread schedulerThread = new Thread(scheduler, "Scheduler");

        try {
            FloorSubsystem floorSubsystem = new FloorSubsystem(scheduler, CSV_FILE_NAME);
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(1, scheduler);
    }
}
