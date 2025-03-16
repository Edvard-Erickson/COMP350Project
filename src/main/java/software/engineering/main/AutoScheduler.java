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

    public ArrayList<Schedule> getSchedulePossibilties() {
        return SchedulePossibilties;
    }

    public void generatePossibleSchedules(){ // might add filters to the param
        //1) grab first section/timeblock

        //2) see if there is mulitple times for that section
        // if so then split, make a schedule for each section seperatly

        //3) split for everyother section seperately (adding each class that is left
        // to possilble scheduled as if it was added second)

        //4) repeat 3 till untill no more sections

        //5) repeat 1-4 for every course


        Schedule tempSch = new Schedule();
        this.SchedulePossibilties = genPossibleSchedulesHelper(SchedulePossibilties,CourseList,tempSch);
        // now we should have a list of schedules to work with

        /*TODO: Filter through the schedules that are most benifical and those that are not
        / Factors:
        /   How early the first class
        /   How many classes are fit into one schedule
        /   is there time for lunch
        /   do they end before practice
        /   are the classes you really wanted to take in the schedule (might be a later addition)
        */
    }

    public ArrayList<Schedule> genPossibleSchedulesHelper(ArrayList<Schedule> SchedulePossibilties, ArrayList<Timeblock> coursesLeft, Schedule curSec) {
        // should be recursive I think it can be done
        // base case is when there is no more courses left in the course left

        //Could this be void since we are adding the schedule to a list that is being passed in

        //Base Case
        if(coursesLeft.isEmpty()) {
            SchedulePossibilties.add(curSec);
            return SchedulePossibilties;
        }

        // go through all courses and add them
        for(int i = 0; i < coursesLeft.size(); i++) {
            // make a new schedule for the split

            //TODO: Add rules to handle conflicts and
            // so timeblocks always take precidence over sections
            // also will have call if there is more than one time section
            curSec.addTimeblock(coursesLeft.get(i));

            //placeholder courses left without the course we just removed
            ArrayList<Timeblock> temp = new ArrayList<>(coursesLeft); //Might Not be a deep Copy IDK
            temp.remove(coursesLeft.get(i));

            genPossibleSchedulesHelper(SchedulePossibilties,temp,curSec);
        }

        // added stub
        return null;
    }


}
