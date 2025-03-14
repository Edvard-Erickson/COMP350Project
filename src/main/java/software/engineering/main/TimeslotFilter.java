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
        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < sectionList.size(); i++) {
            if ((sectionList.get(i).getTimeFrame()[0].equals(startTime) || sectionList.get(i).getTimeFrame()[0].isAfter(startTime))
            && (sectionList.get(i).getTimeFrame()[1].equals(endTime) || sectionList.get(i).getTimeFrame()[1].isBefore(endTime))) {
                returnList.add(sectionList.get(i));
            }
        }

        return returnList;
    }
}
