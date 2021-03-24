package FloorSubsystem;

import SchedulerSubsystem.SchedulerApi;
import model.Event;
import stub.SchedulerClient;
import utill.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * The Floor Subsystem class represents all of the floors in the building
 *
 * @version Feb 27, 2021
 */
public class FloorSubsystem {

    /**
     * @param scheduler the scheduler
     * @param schedule  the list of events
     * @return The map of the floors
     */
    public static Map<Integer, Floor> generateFloors(Config config, SchedulerApi scheduler, List<Event> schedule) throws SocketException {
        Map<Integer, Floor> floors = new HashMap<>();

        Map<Integer, List<Event>> schedule_by_floor = schedule.stream().collect(groupingBy(Event::getFloorNumber));

        int max_floor_number = 0;

        for (Map.Entry<Integer, List<Event>> entry : schedule_by_floor.entrySet()) {
            if (entry.getKey() > max_floor_number) {
                max_floor_number = entry.getKey();
            }
            for (Event event : entry.getValue()) {
                if (event.getCarButton() > max_floor_number) {
                    max_floor_number = event.getCarButton();
                }
            }
        }

        if (max_floor_number > config.getIntProperty("numFloors")) {
            throw new RuntimeException("csv has floor above max");
        }
        max_floor_number = config.getIntProperty("numFloors");

        floors.put(0, new BottomFloor(config, 0, scheduler, schedule_by_floor.getOrDefault(0, new ArrayList<>())));
        floors.put(max_floor_number, new TopFloor(config, max_floor_number, scheduler, schedule_by_floor.getOrDefault(max_floor_number, new ArrayList<>())));

        for (int floor_number = 1; floor_number < max_floor_number; floor_number++) {
            floors.put(floor_number, new MiddleFloor(config, floor_number, scheduler, schedule_by_floor.getOrDefault(floor_number, new ArrayList<>())));
        }
        return floors;
    }

    /**
     * Generates the map of floors in the system
     *
     * @param scheduler         The scheduler
     * @param schedule_filename The input file
     * @return The map of floors
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static Map<Integer, Floor> generateFloors(Config config, SchedulerApi scheduler, String schedule_filename) throws FileNotFoundException, ParseException, SocketException {
        return generateFloors(config, scheduler, FloorSubsystem.readCSV(config, schedule_filename));
    }

    /**
     * Reads the input file and schedules the events
     *
     * @param filename The input file
     * @return The scheduled list of events
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<Event> readCSV(Config config, String filename) throws FileNotFoundException, ParseException {
        List<Event> schedule = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(",");
            schedule.add(new Event((new SimpleDateFormat(config.getProperty("dateFormatPattern"))).parse(config.getProperty("startDate")),
                    new SimpleDateFormat(config.getProperty("dateFormatPattern")).parse("01-01-2021 " + line[0]),
                    Integer.parseInt(line[1]), line[2].equalsIgnoreCase("up"), Integer.parseInt(line[3])));
        }

        scanner.close();
        return schedule;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Config config = new Config();
        SchedulerApi schedulerApi = new SchedulerClient(config, InetAddress.getLocalHost(), config.getIntProperty("schedulerPort"));
        Map<Integer, Floor> floors = generateFloors(config, schedulerApi, config.getProperty("csvFileName"));
        floors.forEach((floorNumber, floor) -> floor.start());
    }
}
