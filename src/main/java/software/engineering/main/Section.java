package software.engineering.main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Section extends Timeblock{
    protected ArrayList<String> prereq; // a list of courses saved as department course code (ie. HUMA220)
    protected String department; // Four Chars (ie. HUMA COMP SSFT)
    protected int courseCode; // three digit code such as (300 200 432)
    protected char section; // a char that repersents each section (ie A B L)
    private String professor; //Full name of the professor

    protected Section(String department, int courseCode, char section, String courseName, String professor, HashMap<String, String[]> times) {
        //Might Need to flesh out
        super(times, courseName);
        this.department = department;
        this.courseCode = courseCode;
        this.section = section;
        this.professor = professor;

        //each
        prereq = new ArrayList<>();
    }

    protected void setPrereqs(ArrayList<String> prereqs) {
        //prereqs saved as course code (i.e. COMP222)
        this.prereq = prereqs;
    }

    protected String getDepartment() {
        return this.department;
    }

    protected int getCourseCode() {
        return this.courseCode;
    }

    protected String getCourseName() {
        return super.getName();
    }

    protected String getProfessor() {
        return professor;
    }

    protected char getSection(){ return this.section;}

}
