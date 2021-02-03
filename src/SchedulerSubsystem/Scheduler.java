package SchedulerSubsystem;

import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
     List<Event> schedule;

    public Scheduler() {
        schedule = new ArrayList<>();
    }

    public void setSchedule(List<Event> schedule) {
        this.schedule = schedule;
    }

    @Override
    public void run() {

    }
}
