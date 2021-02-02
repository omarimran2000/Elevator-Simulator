import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import FloorSubsystem.FloorSubsystem;
import SchedulerSubsystem.Scheduler;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scheduler scheduler = new Scheduler();
        Thread schedulerThread = new Thread(scheduler,"Scheduler");

        FloorSubsystem floorSubsystem = new FloorSubsystem(7,scheduler);
        floorSubsystem.readCSV("test.csv");

        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(1,scheduler);
    }
}
