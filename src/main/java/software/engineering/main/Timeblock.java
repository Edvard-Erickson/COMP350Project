package software.engineering.main;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

    // returns true if the timeblocks overlap at any point, false otherwise
    protected boolean conflictsWith(Timeblock other) {
        Iterator timesI = times.keySet().iterator();


        while (timesI.hasNext()) {
            String day = (String) timesI.next();

            Iterator otherTI = other.getTimes().keySet().iterator();
            while (otherTI.hasNext()) {
                String otherDay = (String) otherTI.next();

                if (day.equals(otherDay)) {
                    String[] timeSlots = times.get(day);
                    String[] otherTimeSlots = other.getTimes().get(otherDay);

                    String start = timeSlots[0];
                    String end = timeSlots[1];
                    String otherStart = otherTimeSlots[0];
                    String otherEnd = otherTimeSlots[1];

                    ArrayList<String> sCon = new ArrayList<String>();

                    sCon.add(start);
                    sCon.add(end);
                    sCon.add(otherStart);
                    sCon.add(otherEnd);
                    for (int i = 0; i < 4; i++) {
                        // TODO: find what length 10:00 is stored as and change the 5 below to that number
                        if (sCon.get(i).length() < 8) {
                            String s = (String) sCon.get(i);
                            sCon.set(i, ("0" + s).substring(0,5));
                        }
                    }

                    LocalTime ltstart = null;
                    LocalTime ltend = null;
                    LocalTime ltotherStart = null;
                    LocalTime ltotherEnd = null;
                    try {
                        ltstart = LocalTime.parse(sCon.get(0));
                        //System.out.println(start);
                        ltend = LocalTime.parse(sCon.get(1));
                        //System.out.println(end);
                        ltotherStart = LocalTime.parse(sCon.get(2));
                        //System.out.println(otherStart);
                        ltotherEnd = LocalTime.parse(sCon.get(3));
                        //System.out.println(otherEnd);
                    } catch (DateTimeParseException e) {
                        System.out.println("uh oh, the date didn't parse correctly");
                    }

                    if (((ltstart.compareTo(ltotherStart) <= 0) && (ltend.compareTo(ltotherStart) > 0)) ||
                            ((ltstart.compareTo(ltotherEnd) <= 0) && (ltend.compareTo(ltotherEnd) > 0))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
