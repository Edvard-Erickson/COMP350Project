package software.engineering.main;

import java.util.ArrayList;

public class AutoScheduler {
    private ArrayList<Schedule> SchedulePossibilties;
    //All courses you would want to take regardless of conflicts
    private ArrayList<Timeblock> CourseList;
    private Search search;

    //Constructor given all courses
    public AutoScheduler(ArrayList<Section> allCourses) {
        this.SchedulePossibilties = new ArrayList<>();
        this.CourseList = new ArrayList<>();
        this.search = new Search(allCourses); // must pass all courses so that it can create a new search object
    }

    //Constructor given a search object
    public AutoScheduler(Search s){
        this.SchedulePossibilties = new ArrayList<>();
        this.CourseList = new ArrayList<>();
        this.search = s; //pass in an already created search object
    }

    //Constructor given a schedule object and all courses
    public AutoScheduler(ArrayList<Section> allCourses, Schedule Sch) {
        this.SchedulePossibilties = new ArrayList<>();
        this.CourseList = Sch.getSchedule();
        this.search = new Search(allCourses); // must pass all courses so that it can create a new search object
    }

    //Constructor given a schedule object and a search object
    public AutoScheduler(Search s, Schedule Sch) {
        this.SchedulePossibilties = new ArrayList<>();
        this.CourseList = Sch.getSchedule();
        this.search = s; //pass in an already created search object
    }

    public void addToPossibleCourseList(Section sec) {
        //Search for all other section and add them to the course list
        this.CourseList.addAll(search.courseSections(sec.getDepartment(), sec.getCourseCode()));
    }
    public void removeFromCourseList(Timeblock sec) {
        this.CourseList.remove(sec);
    }

    public ArrayList<Schedule> getSchedulePossibilities() {
        return SchedulePossibilties;
    }

    public ArrayList<Schedule> getTopTenSchedules() {
        // Sort the SchedulePossibilties list based on the number of courses in each schedule
        SchedulePossibilties.sort((s1, s2) -> Integer.compare(s2.getSchedule().size(), s1.getSchedule().size()));

        // Create a new list to store the top ten schedules
        ArrayList<Schedule> topTenSchedules = new ArrayList<>();

        // Add the top ten schedules to the new list
        for (int i = 0; i < Math.min(10, SchedulePossibilties.size()); i++) {
            topTenSchedules.add(SchedulePossibilties.get(i));
        }

        return topTenSchedules;
    }
    public void generatePossibleSchedules() {
        Schedule initialSchedule = new Schedule();
        backTrackGenerate(initialSchedule, 0); // get all possible schedules
        //TODO: Implement a way to RANK the schedules
    }

    private void backTrackGenerate(Schedule currentSchedule, int index) {
        //MitchellAL 100% code below was written by copilot. I only tested it and made sure it worked

        // Base case: if we have considered all courses, add the current schedule to the possibilities
        if (index == CourseList.size()) {
            SchedulePossibilties.add(new Schedule(currentSchedule));
            return;
        }

        // Get the current course to consider
        Timeblock currentCourse = CourseList.get(index);

        // Check if the current course can be added without conflicts
        if (!hasConflict(currentSchedule, currentCourse)) {
            // Add the current course to the schedule
            currentSchedule.addTimeblock(currentCourse);
            // Recursively try to add the next course
            backTrackGenerate(currentSchedule, index + 1);
            // Backtrack: remove the current course from the schedule
            currentSchedule.removeTimeblock(currentCourse);
        }

        // Recursively try to add the next course without including the current course
        backTrackGenerate(currentSchedule, index + 1);
    }

    private boolean hasConflict(Schedule schedule, Timeblock course) {
        //MitchellAL 100% code below was written by copilot. I only tested it and made sure it worked
        //except for the try block check I added

        for (Timeblock scheduledCourse : schedule.getSchedule()) {
            //check to see if there is a course at that time
            if (course.conflictsWith(scheduledCourse)) {
                return true;
            }

            //Mitchellal I added the try block below
            // to see if there is another section in the course list that conflicts with the current course
            // this is so the Generater does not generate a Schdule with you in two the same class (ie 2 western civs)
            try{
                if(((Section) course).sectionOf((Section) scheduledCourse)){
                    return true;
                }
            } catch (ClassCastException e){
                //do nothing means one is a timeblock and the other is a section so they cant be the same course
            }
        }
        return false;
    }

}
