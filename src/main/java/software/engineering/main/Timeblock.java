package software.engineering.main;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;

public class Timeblock {
    private HashMap<String, String[]> times;
    private String name;
    private String days;

    protected Timeblock(HashMap<String, String[]> times) {
        this.times = times;
        this.name = "";
    }

    protected Timeblock(HashMap<String, String[]> times, String name) {
        this.times = times;
        this.name = name;
    }

    protected void setName(String name) {
        this.name = name; // might want to add some kind of limit
    }

    public HashMap<String, String[]> getTimes() {
        return times;
    }

    public String getDays() {
        return days;
    }

    protected String getName() {
        return name;
    }

    // TODO: this needs to be tested
    // returns true if the timeblocks overlap at any point, false otherwise
    protected boolean conflictsWith(Timeblock other) {
        Iterator timesI = times.keySet().iterator();
        Iterator otherTI = other.getTimes().keySet().iterator();

        while (timesI.hasNext()) {
            while (otherTI.hasNext()) {
                String day = (String) timesI.next();
                String otherDay = (String) otherTI.next();

                if (day.equals(otherDay)) {
                    String[] timeSlots = times.get(day);
                    String[] otherTimeSlots = other.getTimes().get(otherDay);

                    String start = timeSlots[0];
                    String end = timeSlots[0];
                    String otherStart = timeSlots[1];
                    String otherEnd = timeSlots[1];

                    if ((start.compareTo(otherStart) <= 0) && (end.compareTo(otherStart) > 0) ||
                            (start.compareTo(otherEnd) <= 0) && (end.compareTo(otherEnd) > 0)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
