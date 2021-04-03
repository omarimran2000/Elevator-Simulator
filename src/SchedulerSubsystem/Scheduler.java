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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The Scheduler class schedules the events
 *
 * @version Feb 27, 2021
 */
public class Scheduler extends Thread implements SchedulerApi {
    private final Logger logger;
    private final DatagramSocket socket;
    private final Config config;
    private final ConcurrentSkipListSet<Destination> destinations;
    private final GuiApi gui;
    private List<ElevatorApi> elevators;
    private Map<Integer, FloorApi> floors;


    public Scheduler(Config config, GuiApi gui) throws SocketException {
        this.gui = gui;
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
        socket = new DatagramSocket(config.getIntProperty("schedulerPort"));
        destinations = new ConcurrentSkipListSet<>();
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
        Optional<ElevatorApi> elevatorOptional = elevators.stream().filter(elevator -> {
            try {
                return elevator.canAddDestination(destination);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
        })
                .min(Comparator.comparing(elevator -> {
                    try {
                        return elevator.distanceTheFloor(destination);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new UndeclaredThrowableException(e);
                    }
                }));
        if (elevatorOptional.isPresent()) {
            elevatorOptional.get().addDestination(destination);
        } else {
            destinations.add(destination);
            gui.addSchedulerDestination(destination.getFloorNumber(), destination.isUp());
        }
    }

    /**
     * Returns the set of people waiting to go up at a specific floor
     *
     * @param floorNumber
     * @return Floors
     */
    @Override
    public HashSet<Integer> getWaitingPeople(int floorNumber) {
        logger.info("Stopped elevator on floor " + floorNumber + " asking for more destinations.");
        HashSet<Destination> destinations = new HashSet<>(this.destinations.stream()
                .collect(Collectors.groupingBy(destination -> destination.getFloorNumber() > floorNumber)).values().stream()
                .max(Comparator.comparingInt(List::size)).orElse(new ArrayList<>()));
        logger.info("returning " + destinations.size() + " to the stopped elevator");
        if (destinations.size() > 0) {
            this.destinations.removeAll(destinations);
            try {
                gui.removeSchedulerDestinations(destinations);
            } catch (IOException | ClassNotFoundException e) {
                throw new UndeclaredThrowableException(e);
            }
            return (HashSet<Integer>) destinations.stream().map(Destination::getFloorNumber).collect(Collectors.toSet());
        }
        return new HashSet<>();
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
                            return getWaitingPeopleUp((int) input.get(0));
                        } catch (IOException | ClassNotFoundException e) {
                            throw new UndeclaredThrowableException(e);
                        }
                    },
                    2, input -> {
                        try {
                            return getWaitingPeopleDown((int) input.get(0));
                        } catch (IOException | ClassNotFoundException e) {
                            throw new UndeclaredThrowableException(e);
                        }
                    },
                    3, input -> {
                        try {
                            handleFloorButton((Destination) input.get(0));
                            return new AckMessage();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new UndeclaredThrowableException(e);
                        }
                    }, 4, input -> getWaitingPeople((Integer) input.get(0)),
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
     * Interupt method
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
     * @param floorNumber The corresponding floor number for the requests
     * @return Floors The Floors object of people waiting to go up
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public HashSet<Integer> getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException {
        logger.info("getting people wait to go up on floor " + floorNumber);
        return floors.get(floorNumber).getWaitingPeopleUp();
    }

    /**
     * Get waiting people down
     *
     * @param floorNumber the corresponding floor number
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public HashSet<Integer> getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException {
        logger.info("getting people wait to go down on floor " + floorNumber);
        return floors.get(floorNumber).getWaitingPeopleDown();
    }
}
