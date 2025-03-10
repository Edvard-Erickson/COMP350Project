package software.engineering.main;

import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<Timeblock> timeblocks;

    protected Schedule() {
    }

    protected void addSection(Section section) {
        for (int i = 0; i < timeblocks.size(); i++) {
            if (isValid(timeblocks.get(i), section) ) { // if there is no conflict and the additon is valid then add it to the arraylist
                timeblocks.add(section);
            }
        }
    }

    protected void addTimeblock(Timeblock block) {
    }

    protected ArrayList<Object> getSchedule() {
        return null;
    }

    protected boolean isValid(Timeblock block1, Timeblock block2) {
        //returns true if there is no conflict
        boolean isConflict = false;

        LocalTime start1 = block1.getTimeFrame()[0];
        LocalTime end1 = block1.getTimeFrame()[1];

        LocalTime start2 = block2.getTimeFrame()[0];
        LocalTime end2 = block2.getTimeFrame()[1];

        //case for same exact time slot being added
        if(start1.equals(start2) && end1.equals(end2)) {
            isConflict = false;
            System.exit(0);
        }

        // case where class 1 is before 2 and 2 starts at the same time
        if( (end1.isBefore(start2) || end1.equals(start2)) && (end1.isBefore(end2))) {
            isConflict = true;
        }

        //3 case where class 2 is before 1 and 1 starts at the same time
        if((end2.isBefore(start1) || end2.equals(start1)) && (end2.isBefore(end1))) {
            isConflict = true;
        }

        if( ( end1.isBefore(start2) && end1.isBefore(end2) ) || ( end2.isBefore(start1) && end2.isBefore(end1) ) ) {
            System.out.println("4) Class do not overlap");
            isConflict = true;
        }

        return isConflict;
    }
}
