package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;
import utill.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ElevatorSubsystem {

    public static List<Elevator> generateElevators(Config config, Scheduler scheduler, int maxFloor) {
        List<Elevator> elevators = new ArrayList<>();

        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            Elevator tempElev = new Elevator(config, scheduler, i + 1, maxFloor);
            elevators.add(tempElev);
            new Thread(tempElev).start();
        }
        return elevators;
    }
}
