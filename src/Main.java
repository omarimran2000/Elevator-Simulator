import SchedulerSubsystem.Scheduler;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scheduler scheduler = new Scheduler();
        scheduler.readCSV("test.csv");
    }
}
