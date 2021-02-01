package SchedulerSubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Scheduler implements Runnable {
    ArrayList<HashMap<String,String>> schedule;
    public Scheduler() {
        schedule = new ArrayList<HashMap<String, String>>();
    }
    public void readCSV(String filename) throws FileNotFoundException {
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
    }

    @Override
    public void run() {

    }
}
