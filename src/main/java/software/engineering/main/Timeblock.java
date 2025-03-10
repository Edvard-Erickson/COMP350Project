package software.engineering.main;

import java.time.LocalTime;

public class Timeblock {
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;

    protected Timeblock(LocalTime start, LocalTime end) throws IllegalArgumentException {
        if(start.isBefore(end)) {
            this.startTime = start;
            this.endTime = end;
            this.name = "";
        }else{
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    protected Timeblock(LocalTime start, LocalTime end, String name) throws IllegalArgumentException {
        if(start.isBefore(end)){
            startTime = start;
            endTime = end;
            this.name = name;
        }else{
            throw new IllegalArgumentException("Start Time must be before End Time");
        }
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
}
