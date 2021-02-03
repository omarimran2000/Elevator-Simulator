package FloorSubsystem;

import SchedulerSubsystem.Event;
import SchedulerSubsystem.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class FloorSubsystem {
    public static final SimpleDateFormat CSV_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private final Scheduler scheduler;
    private final Set<Thread> floors;

    public FloorSubsystem(Scheduler scheduler, List<Event> schedule) {
        this.scheduler = scheduler;

        floors = new HashSet<>();

        Map<Integer, List<Event>> schedule_by_floor = schedule.stream().collect(groupingBy(Event::getFloor));

        int max_floor_number = 0;

        for (int floor_number : schedule_by_floor.keySet()) {
            if (floor_number > max_floor_number) {
                max_floor_number = floor_number;
            }
        }

        for (int floor_number = 0; floor_number < max_floor_number; floor_number++) {
            Thread temp = new Thread(new Floor(scheduler, schedule_by_floor.get(floor_number)), "Floor " + floor_number);
            temp.start();
            floors.add(temp);
        }
    }

    public FloorSubsystem(Scheduler scheduler, String schedule_filename) throws FileNotFoundException, ParseException {
        this(scheduler, FloorSubsystem.readCSV(schedule_filename));
    }

    public static List<Event> readCSV(String filename) throws FileNotFoundException, ParseException {
        List<Event> schedule = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(",");
            schedule.add(new Event(CSV_DATE_FORMAT.parse(line[0]), Integer.parseInt(line[1]), line[2].equalsIgnoreCase("up"), Integer.parseInt(line[3])));
        }

        scanner.close();
        return schedule;
    }
}