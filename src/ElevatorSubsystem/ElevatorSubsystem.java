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
            Elevator tempElev = new Elevator(config, scheduler, i + 1, maxFloor);
            elevators.add(tempElev);
            new Thread(tempElev).start();
        }
        return elevators;
    }

    public static void main(String[] args) throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        Config config = new Config();
        SchedulerApi schedulerApi = new SchedulerClient(config, localhost, config.getIntProperty("schedulerPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, config.getIntProperty("maxFloors"));
    }
}
