package FloorSubsystem;

import SchedulerSubsystem.Scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FloorSubsystem {

    private static int NUM_FLOORS;
    private Scheduler scheduler;
    private ArrayList<Thread> floors;

    public FloorSubsystem(int numFloors,Scheduler scheduler) {
        this.NUM_FLOORS = numFloors;
        this.scheduler = scheduler;


        floors = new ArrayList<>();

        for(int i=0;i<NUM_FLOORS;i++)
        {
            Thread temp = new Thread(new Floor(scheduler),"Floor "+(i+1));
            temp.start();
            floors.add(temp);
        }
    }
    public void readCSV(String filename) throws FileNotFoundException {
        ArrayList<HashMap<String,String>> schedule = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));

        String [] keys = {"time","floor","floor button","car button"};
        int rowCount = 0;

        while(scanner.hasNext())
        {
            schedule.add(new HashMap<>());
            String[] line = scanner.nextLine().split(",");

            int lineCount = 0;

            while(lineCount<line.length)
            {
                schedule.get(rowCount).put(keys[lineCount],line[lineCount]);
                lineCount++;
            }

            rowCount++;
        }
        scanner.close();
        scheduler.setSchedule(schedule);
    }
}
