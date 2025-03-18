package software.engineering.main;

import java.util.HashMap;

public class Section extends Timeblock{
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

    protected boolean sectionOf(Section other) {
        return this.department.equals(other.getDepartment()) && this.courseCode == other.getCourseCode();
    }
}
