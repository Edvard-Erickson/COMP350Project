package software.engineering.main;

import java.util.ArrayList;

public class AutoScheduler {
    private ArrayList<Schedule> SchedulePossibilties;
    //All courses you would want to take regardless of conflicts
    private ArrayList<Timeblock> CourseList;

    //Constructor given no input
    public AutoScheduler() {
        this.SchedulePossibilties = new ArrayList<>();
        this.CourseList = new ArrayList<>();
    }

    //Constructor given a schedule object
    public AutoScheduler(Schedule Sch) {
        this.SchedulePossibilties = new ArrayList<>();
        this.CourseList = new ArrayList<>();

        //make a copy of
    }

    public void addToCourseList(Timeblock sec) {
        this.CourseList.add(sec);
    }
    public void removeFromCourseList(Timeblock sec) {
        this.CourseList.remove(sec);
    }
}
