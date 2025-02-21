package software.engineering.main;

import java.time.LocalTime;
import java.util.ArrayList;

public class Section extends Timeblock{
    protected ArrayList<String> prereq;
    protected String department;
    protected String courseName;
    protected String courseCode;
    protected char section;
    private String professor;

    protected Section(String department, String courseCode, char section, String courseName, String professor, LocalTime startTime, LocalTime endTime) {
        super(startTime, endTime);
    }

    protected void setPrereqs(ArrayList<String> prereqs) {
    }

    protected String getDepartment() {
        return null;
    }

    protected int getCourseCode() {
        return -1;
    }

    protected String getCourseName() {
        return null;
    }

    protected String getProfessor() {
        return professor;
    }

    protected LocalTime[] getTimeFrame() {
        return super.getTimeFrame();
    }

    protected boolean hasConflict(Section other) {
        return false;
    }
}
