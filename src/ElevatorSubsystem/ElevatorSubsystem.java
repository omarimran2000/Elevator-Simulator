package ElevatorSubsystem;

import SchedulerSubsystem.Scheduler;
import utill.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ElevatorSubsystem {
    private final List<Elevator> elevators;

    public ElevatorSubsystem(Config config, Scheduler scheduler, int maxFloor) {
        elevators = new ArrayList<>();

        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            Elevator tempElev = new Elevator(config, scheduler, i + 1, maxFloor);
            elevators.add(tempElev);
            new Thread(tempElev).start();
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}
