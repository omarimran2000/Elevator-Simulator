package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;
import util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * The Floor Subsystem class represents all of the floors in the building
 *
 * @version Feb 06, 2021
 */
public class FloorSubsystem {
    public static final SimpleDateFormat CSV_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.S");
    private static Date START_DATE;

    static {
        try {
            START_DATE = CSV_DATE_FORMAT.parse("14:00:00.0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param scheduler the scheduler
     * @param schedule  the list of events
     * @return The map of the floors
     */
    public static Map<Integer, Floor> generateFloors(Scheduler scheduler, List<Event> schedule) {
        Map<Integer, Floor> floors = new HashMap<>();

        Map<Integer, List<Event>> schedule_by_floor = schedule.stream().collect(groupingBy(Event::getFloor));

        int max_floor_number = 0;

        for (int floor_number : schedule_by_floor.keySet()) {
            if (floor_number > max_floor_number) {
                max_floor_number = floor_number;
            }
        }

        floors.put(0, new BottomFloor(0, scheduler, schedule_by_floor.getOrDefault(0, new ArrayList<>())));
        floors.put(max_floor_number, new TopFloor(max_floor_number, scheduler, schedule_by_floor.getOrDefault(max_floor_number, new ArrayList<>())));

        for (int floor_number = 1; floor_number < max_floor_number; floor_number++) {
            floors.put(floor_number, new MiddleFloor(floor_number, scheduler, schedule_by_floor.getOrDefault(floor_number, new ArrayList<>())));
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
    public static Map<Integer, Floor> generateFloors(Config config, Scheduler scheduler, String schedule_filename) throws FileNotFoundException, ParseException {
        return generateFloors(scheduler, FloorSubsystem.readCSV(config, schedule_filename));
    }

    /**
     * Reads the input file and schedules the events
     *
     * @param filename The input file
     * @param config
     * @return The scheduled list of events
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<Event> readCSV(Config config, String filename) throws FileNotFoundException, ParseException {
        List<Event> schedule = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        SimpleDateFormat csvDateFormat = new SimpleDateFormat(config.getProperty("dateFormatPattern"));

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(",");
            schedule.add(new Event(Duration.between(csvDateFormat.parse(config.getProperty("startDate")).toInstant(), CSV_DATE_FORMAT.parse(line[0]).toInstant()).toSeconds(), Integer.parseInt(line[1]), line[2].equalsIgnoreCase("up"), Integer.parseInt(line[3])));
        }

        scanner.close();
        return schedule;
    }
}
