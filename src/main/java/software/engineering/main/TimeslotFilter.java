package software.engineering.main;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeslotFilter extends Filter {

    private HashMap<String, String[]> times;

    public TimeslotFilter(HashMap<String, String[]> times) {
        super();
        this.times = times;
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        Timeblock b = new Timeblock(times);
        ArrayList<Section> returnList = new ArrayList<Section>();

        for (int i = 0; i < sectionList.size(); i++) {
            if (b.conflictsWith(sectionList.get(i))) {
                returnList.add(sectionList.get(i));
            }
        }

        return returnList;
    }
}
