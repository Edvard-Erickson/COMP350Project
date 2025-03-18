package software.engineering.main;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<Section> classes;
    private ArrayList<Timeblock> blocks;

    protected Schedule() {
        classes = new ArrayList<Section>();
        blocks = new ArrayList<Timeblock>();
    }
    protected Schedule(Schedule schedule){
        this.classes = new ArrayList<>(schedule.classes);
        this.blocks = new ArrayList<>(schedule.blocks);

    }

    protected int addSection(Section section) {
        if(!hasConflict(section)){
            classes.add(section);
            return 0;
        }else{
            System.out.println("Error: Section conflicts with another section");
            return 1; //error Courses conflict
        }
    }

    protected int addTimeblock(Timeblock block) {
        if(!hasConflict(block)) {
            blocks.add(block);
            return 0;
        }else{
            System.out.println("Error: Timeblock conflicts with another timeblock");
            return 1; //error Courses conflict
        }
    }

    protected void removeSection(Section section) {
        classes.remove(section);
    }

    protected void removeTimeblock(Timeblock block) {
        blocks.remove(block);
    }

    protected ArrayList<Timeblock> getSchedule() {
        ArrayList<Timeblock> toReturn = new ArrayList<Timeblock>();
        toReturn.addAll(blocks);
        toReturn.addAll(classes);
        return toReturn;
    }

    //May never get used more than testing
    protected void printSchedule() {
        System.out.println("--- Schedule ---");
        for (Timeblock block : blocks) {
            System.out.println(block.getName());
        }
        for (Section section : classes) {
            System.out.println(section.getCourseName());
        }

    }

    //this hasConflict() allows one to add the same course multiple sections to the schdeule
    private boolean hasConflict(Timeblock course) {
        for (Timeblock scheduledCourse : getSchedule()) {
            //check to see if there is a course at that time
            if (course.conflictsWith(scheduledCourse)) {
                return true;
            }
        }
        return false;
    }
}
