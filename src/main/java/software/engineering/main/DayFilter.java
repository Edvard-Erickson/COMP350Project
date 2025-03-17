package software.engineering.main;

import java.time.LocalTime;
import java.util.ArrayList;

public class DayFilter extends Filter {

    protected String days;

    public DayFilter(String days) {
        super();
        this.days = days;
    }

    // not complete
    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        ArrayList<Section> returnList = new ArrayList<Section>();

        return returnList;
    }
}