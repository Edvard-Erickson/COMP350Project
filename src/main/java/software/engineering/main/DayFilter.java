package software.engineering.main;

import java.time.LocalTime;
import java.util.ArrayList;

public class DayFilter extends Filter {

    protected LocalTime startTime;
    protected LocalTime endTime;

    public DayFilter(LocalTime startTime, LocalTime endTime) {
        super();
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        ArrayList<Section> returnList = new ArrayList<Section>();

        return returnList;
    }
}