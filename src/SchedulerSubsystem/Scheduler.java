package SchedulerSubsystem;


import ElevatorSubsystem.ElevatorApi;
import FloorSubsystem.FloorApi;
import model.AckMessage;
import model.Destination;
import model.SendSet;
import stub.ElevatorClient;
import stub.FloorClient;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;
import java.util.logging.Logger;

/**
 * The Scheduler class schedules the events
 *
 * @version Feb 27, 2021
 */
public class Scheduler extends Thread implements SchedulerApi {
    private final Logger logger;
    private List<ElevatorApi> elevators;
    private Map<Integer, FloorApi> floors;
    private final DatagramSocket socket;
    private final Config config;


    public Scheduler(Config config) throws SocketException {
        logger = Logger.getLogger(this.getClass().getName());
        this.config = config;
        socket = new DatagramSocket(config.getIntProperty("schedulerPort"));
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
     * Set the map of floors in the system
     *
     * @param floors The map of floors in the system
     */
    public void setFloors(Map<Integer, FloorApi> floors) {
        if (this.floors == null) {
            this.floors = Collections.unmodifiableMap(floors);
        }
    }

    @Override
    public void handleFloorButton(Destination destination) throws IOException, ClassNotFoundException {
        logger.info("Scheduler: scheduling event for floor " + destination.getFloorNumber());
        elevators.stream()
                .min(Comparator.comparing(elevator -> {
                    try {
                        return elevator.distanceTheFloor(destination);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new UndeclaredThrowableException(e);
                    }
                }))
                .orElseThrow(NoSuchElementException::new)
                .addDestination(destination);
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
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        // close socket to interrupt receive
        socket.close();
    }

    @Override
    public SendSet getWaitingPeopleUp(int floorNumber) throws IOException, ClassNotFoundException {
        logger.info("getting people wait to go up on floor " + floorNumber);
        return floors.get(floorNumber).getWaitingPeopleUp();
    }

    @Override
    public SendSet getWaitingPeopleDown(int floorNumber) throws IOException, ClassNotFoundException {
        logger.info("getting people wait to go down on floor " + floorNumber);
        return floors.get(floorNumber).getWaitingPeopleDown();
    }

    public static void main(String[] args) throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        Config config = new Config();
        Scheduler scheduler = new Scheduler(config);
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
}
