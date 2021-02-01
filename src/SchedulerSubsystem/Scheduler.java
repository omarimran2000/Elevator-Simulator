package SchedulerSubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Scheduler implements Runnable {
    public Scheduler() {
    }
    public void readCSV(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("test.csv"));
        scanner.useDelimiter(",");
        while(scanner.hasNext())
        {
            System.out.print(scanner.next());
        }
        scanner.close();
    }

    @Override
    public void run() {

    }
}
