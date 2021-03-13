package ElevatorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import stub.SchedulerClient;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorSubsystem {

    public static List<Elevator> generateElevators(Config config, SchedulerApi scheduler, int maxFloor) throws SocketException {
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
