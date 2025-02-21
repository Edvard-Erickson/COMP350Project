package software.engineering.main;

import java.time.LocalTime;
import java.util.ArrayList;

public class TimeslotFilter extends Filter {

    protected LocalTime startTime;
    protected LocalTime endTime;

    public TimeslotFilter(LocalTime startTime, LocalTime endTime) {
        super();
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        return null;
    }
}
