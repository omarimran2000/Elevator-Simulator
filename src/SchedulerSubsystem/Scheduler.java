package SchedulerSubsystem;

import java.util.*;

public class Scheduler implements Runnable {
    ArrayList<HashMap<String,String>> schedule;
    public Scheduler() {
        schedule = new ArrayList<HashMap<String, String>>();
    }

    public void setSchedule(ArrayList<HashMap<String, String>> schedule) {
        this.schedule = schedule;
    }

    @Override
    public void run() {

    }
}
