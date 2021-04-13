import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.FloorSubsystem;
import GUI.GUI;
import SchedulerSubsystem.Scheduler;

import java.io.IOException;
import java.text.ParseException;

/**
 * Main class that runs all four main functions
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException {
        GUI.main(args);
        Scheduler.main(args);
        ElevatorSubsystem.main(args);
        FloorSubsystem.main(args);
    }
}
