package software.engineering.main;

import java.time.LocalTime;

public class Timeblock {
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    private String days;

    protected Timeblock(LocalTime start, LocalTime end) {
        this.startTime = start;
        this.endTime = end;
        this.name = "";
    }

    protected Timeblock(LocalTime start, LocalTime end, String name) {
        startTime = start;
        endTime = end;
        this.name = name;
    }

    protected void setName(String name) {
        this.name = name; // might want to add some kind of limit
    }

    protected LocalTime[] getTimeFrame() {
        return new LocalTime[]{startTime, endTime};
    }

    protected String getName() {
        return name;
    }

    protected boolean conflictsWith(Timeblock other) {
        if (((other.getTimeFrame()[0].equals(startTime) || other.getTimeFrame()[0].isAfter(startTime))
                && other.getTimeFrame()[0].isBefore(endTime)) ||
                ((other.getTimeFrame()[1].equals(startTime) || other.getTimeFrame()[1].isAfter(startTime))
                && other.getTimeFrame()[1].isBefore(endTime))
        ) {
            return true;
        }

        return false;
    }
}
