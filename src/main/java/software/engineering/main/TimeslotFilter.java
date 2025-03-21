package software.engineering.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimeslotFilter extends Filter {

    private HashMap<String, String[]> times;
    private HashMap<String, String[]> beforeTimes;
    private HashMap<String, String[]> afterTimes;

    public TimeslotFilter(HashMap<String, String[]> times) {
        super();
        this.times = times;
        beforeTimes = new HashMap<String, String[]>();
        afterTimes = new HashMap<String, String[]>();

        for (String key : new String[]{"M", "T", "W", "R", "F"}) {
            if (!times.containsKey(key)) {
                String[] beforeTime = new String[2];
                beforeTime[0] = "00:00:00";
                beforeTime[1] = "23:59:59";
                beforeTimes.put(key, beforeTime);
                String[] after = new String[2];
                after[0] = "00:00:00";
                after[1] = "23:59:59";
                afterTimes.put(key, after);
            } else {
                String[] time = times.get(key);
                String[] beforeTime = new String[2];
                beforeTime[0] = "00:00:00";
                beforeTime[1] = time[0];
                beforeTimes.put(key, beforeTime);
                String[] after = new String[2];
                after[0] = time[1];
                after[1] = "23:59:59";
                afterTimes.put(key, after);
            }
        }
    }

    public boolean isFullWeekAllDay() {
        List<String> days = Arrays.asList("M", "T", "W", "R", "F");
        for (String day : days) {
            if (!times.containsKey(day)) {
                return false;
            }
            String[] time = times.get(day);
            if (!time[0].equals("00:00") || !time[1].equals("24:00")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> sectionList) {
        System.out.println(times.keySet());
        Timeblock b = new Timeblock(beforeTimes);
        Timeblock a = new Timeblock(beforeTimes);
        ArrayList<Section> returnList = new ArrayList<Section>();

        if (isFullWeekAllDay()) {
            return sectionList;
        }

        for (int i = 0; i < sectionList.size(); i++) {
            if (sectionList.get(i).getTimes() == null || sectionList.get(i).getTimes().isEmpty()) {
                continue;
            }
            if (!b.conflictsWith(sectionList.get(i)) && !a.conflictsWith(sectionList.get(i))) {
                returnList.add(sectionList.get(i));
            }
        }

        return returnList;
    }
}
