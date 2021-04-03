package ElevatorSubsystem;

import GUI.GuiApi;
import SchedulerSubsystem.SchedulerApi;
import stub.GuiClient;
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
    public static List<Elevator> generateElevators(Config config, SchedulerApi scheduler, GuiApi gui, int maxFloor) throws IOException, ClassNotFoundException {
        List<Elevator> elevators = new ArrayList<>();

        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            Elevator tempElev = new Elevator(config, scheduler, gui, i, maxFloor);
            tempElev.start();
            elevators.add(tempElev);
        }
        return elevators;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Config config = new Config();
        GuiApi guiApi = new GuiClient(config, InetAddress.getLocalHost(), config.getIntProperty("GUIPort"));
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        ElevatorSubsystem.generateElevators(config, schedulerApi, guiApi, config.getIntProperty("numFloors"));
    }
}
