package ElevatorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import stub.SchedulerClient;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for the ElevatorSubsystem
 *
 * @version March 13th 2021
 */
public class ElevatorSubsystem {
    /**
     * Generate elevators
     *
     * @param config    config file
     * @param scheduler scheduler
     * @param maxFloor  the max floor in the system
     * @return the elevators
     * @throws SocketException
     */
    public static List<Elevator> generateElevators(Config config, SchedulerApi scheduler, int maxFloor) throws IOException {
        List<Elevator> elevators = new ArrayList<>();

        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            Elevator tempElev = new Elevator(config, scheduler, i, maxFloor);
            tempElev.start();
            elevators.add(tempElev);
        }
        return elevators;
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, config.getIntProperty("numFloors"));
    }
}
