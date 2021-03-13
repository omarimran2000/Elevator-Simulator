import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorApi;
import ElevatorSubsystem.ElevatorSubsystem;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import SchedulerSubsystem.SchedulerApi;
import stub.ElevatorClient;
import stub.FloorClient;
import utill.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static FloorSubsystem.FloorSubsystem.generateFloors;

/**
 * This is the main class that starts the threads
 *
 * @version Feb 27, 2021
 */
public class Main {

    /**
     * Initializes the map of floors, the scheduler and the elevator
     * Starts the threads
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        Config config = new Config();
        Scheduler scheduler = new Scheduler(config);
        SchedulerApi schedulerApi = new stub.Scheduler(config,localhost,config.getIntProperty("schedulerPort") );
        try {
            Map<Integer, Floor> floors = generateFloors(config, schedulerApi, config.getProperty("csvFileName"));

            scheduler.setFloors(floors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new FloorClient(config, localhost, config.getIntProperty("floorPort") + e.getKey()))));

            List<Elevator> elevators = ElevatorSubsystem.generateElevators(config, schedulerApi, Collections.max(floors.keySet()));

            List<ElevatorApi> elevatorClients = new ArrayList<>();
            for (int i = 0; i < elevators.size(); i++) {
                elevatorClients.add(new ElevatorClient(config, localhost, config.getIntProperty("elevatorPort") + i));
            }
            scheduler.setElevators(elevatorClients);
            floors.forEach((floorNumber, floor) -> new Thread(floor, "Floor " + floorNumber).start());
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        new Thread(scheduler, "Scheduler").start();
    }
}
