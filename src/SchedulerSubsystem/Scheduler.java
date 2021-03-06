package SchedulerSubsystem;


import ElevatorSubsystem.ElevatorApi;
import FloorSubsystem.FloorApi;
import GUI.GuiApi;
import model.AckMessage;
import model.Destination;
import stub.ElevatorClient;
import stub.FloorClient;
import stub.GuiClient;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The Scheduler class schedules the events
 *
 * @version April 4, 2021
 */
public class Scheduler extends Thread implements SchedulerApi {
    private final Logger logger;
    private final DatagramSocket socket;
    private final Config config;
    private final Set<Destination> destinations;
    private final GuiApi gui;
    private List<ElevatorApi> elevators;
    private Map<Integer, FloorApi> floors;


    /**
     * Creates a scheduler
     *
     * @param config The config file
     * @param gui    The gui
     * @throws SocketException
     */
    public Scheduler(Config config, GuiApi gui) throws SocketException {
        this.gui = gui;
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
        socket = new DatagramSocket(config.getIntProperty("schedulerPort"));
        destinations = new HashSet<>();
    }

    public static void main(String[] args) throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        Config config = new Config();
        GuiApi guiApi = new GuiClient(config, localhost, config.getIntProperty("GUIPort"));
        Scheduler scheduler = new Scheduler(config, guiApi);
        Map<Integer, FloorApi> floors = new HashMap<>();
        for (int i = 0; i < config.getIntProperty("numFloors"); i++) {
            floors.put(i, new FloorClient(config, localhost, config.getIntProperty("floorPort") + i));
        }
        scheduler.setFloors(floors);
        List<ElevatorApi> elevatorClients = new ArrayList<>();
        for (int i = 0; i < config.getIntProperty("numElevators"); i++) {
            elevatorClients.add(new ElevatorClient(config, localhost, config.getIntProperty("elevatorPort") + i));
        }
        scheduler.setElevators(elevatorClients);
        scheduler.start();
    }

    /**
     * Set the map of floors in the system
     *
     * @param floors The map of floors in the system
     */
    public void setFloors(Map<Integer, FloorApi> floors) {
        if (this.floors == null) {
            this.floors = Collections.unmodifiableMap(floors);
        }
    }

    /**
     * Handles a floorButton push request and adds its destination to one of the elevators
     *
     * @param destination The current destination of the elevator event
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException {
        List<ElevatorApi> elevatorByDistance = elevators.stream()
                .sorted(Comparator.comparing(elevator -> {
                    try {
                        return elevator.distanceTheFloor(destination);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new UndeclaredThrowableException(e);
                    }
                })).collect(Collectors.toList());
        for (ElevatorApi elevatorApi : elevatorByDistance) {
            if (elevatorApi.addDestination(destination)) {
                return;
            }
        }
        synchronized (destinations) {
            destinations.add(destination);
        }
        gui.addSchedulerDestination(destination.getFloorNumber(), destination.isUp());
    }

    /**
     * Returns the set of destinations with people waiting to go up at a specific floor
     *
     * @param floorNumber The floor number
     * @return The set of destinations
     */
    @Override
    public HashSet<Destination> getUnscheduledPeople(int floorNumber) {
        logger.info("Stopped elevator on floor " + floorNumber + " asking for more destinations.");
        HashSet<Destination> output;
        synchronized (destinations) {
            if (destinations.isEmpty()) {
                return new HashSet<>();
            }
            output = new HashSet<>(destinations.stream()
                    .filter(destination -> destination.getFloorNumber() > floorNumber == destination.isUp())
                    .collect(Collectors.groupingBy(destination -> destination.getFloorNumber() > floorNumber)).values().stream()
                    .max(Comparator.comparingInt(List::size)).orElse(new ArrayList<>()));
            if (output.isEmpty()) {
                output = new HashSet<>(destinations.stream()
                        .collect(Collectors.groupingBy(destination -> destination.getFloorNumber() > floorNumber)).values().stream()
                        .max(Comparator.comparingInt(List::size)).orElse(new ArrayList<>()));
            }
            destinations.removeAll(output);
        }
        try {
            gui.removeSchedulerDestinations(output);
        } catch (IOException | ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }
        return output;
    }


    /**
     * The run method
     */
    @Override
    public void run() {
        try {
            StubServer.receiveAsync(socket, config.getIntProperty("numHandlerThreads"), config.getIntProperty("maxMessageSize"), Map.of(
                    1, input -> {
                        try {
                            return getWaitingPeople((Destination) input.get(0));
                        } catch (IOException | ClassNotFoundException e) {
                            throw new UndeclaredThrowableException(e);
                        }
                    },
                    2, input -> {
                        try {
                            handleFloorButton((Destination) input.get(0));
                            return new AckMessage();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new UndeclaredThrowableException(e);
                        }
                    }, 3, input -> getUnscheduledPeople((Integer) input.get(0)),
                    20, input -> {
                        interrupt();
                        return new AckMessage();
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method for the elevators List
     *
     * @return elevators The List of elevators
     */
    public List<ElevatorApi> getElevators() {
        return elevators;
    }

    /**
     * Set the list of elevators
     *
     * @param elevators The elevators
     */
    public void setElevators(List<ElevatorApi> elevators) {
        if (this.elevators == null) {
            this.elevators = Collections.unmodifiableList(elevators);
        }
    }

    /**
     * Interrupt method
     */
    @Override
    public void interrupt() {
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    /**
     * Get waiting people up
     *
     * @param destination The corresponding floor number for the requests
     * @return set of destination numbers
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public HashSet<Integer> getWaitingPeople(Destination destination) throws IOException, ClassNotFoundException {
        logger.info("getting people wait to go up on floor " + destination.getFloorNumber());
        return floors.get(destination.getFloorNumber()).getWaitingPeople(destination.isUp());
    }
}
