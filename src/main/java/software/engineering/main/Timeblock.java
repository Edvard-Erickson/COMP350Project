package software.engineering.main;

import java.time.LocalTime;

public class Timeblock {
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;

    protected Timeblock(LocalTime start, LocalTime end) {
    }

    protected LocalTime[] getTimeFrame() {
        return null;
    }

    protected String getName() {
        return name;
    }
}
