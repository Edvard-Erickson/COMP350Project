package software.engineering.main;

import java.util.HashMap;

public class Section extends Timeblock{
    protected String department; // Four Chars (ie. HUMA COMP SSFT)
    protected int courseCode; // three digit code such as (300 200 432)
    protected char section; // a char that repersents each section (ie A B L)
    private String professor; //Full name of the professor
    private String semester;

    protected Section(String department, int courseCode, char section, String courseName, String professor, String semester, HashMap<String, String[]> times) {
        //Might Need to flesh out
        super(times, courseName);
        this.department = department;
        this.courseCode = courseCode;
        this.section = section;
        this.professor = professor;
        this.semester = semester;
    }

    public boolean containsText(String text) {
        if (text.length() >= 1) {
            if (text.charAt(0) == '\"') {
                if (text.length() >= 2) {
                    text = text.substring(1, text.length() - 1);

                    if (getCourseName().contains(text) ||
                            professor.contains(text) ||
                            department.contains(text) ||
                            (text.contains(Integer.toString(courseCode)) && text.contains(department))
                    ) {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                if (getCourseName().toLowerCase().contains(text) ||
                        professor.toLowerCase().contains(text) ||
                        department.toLowerCase().contains(text) ||
                        Integer.toString(courseCode).contains(text)

                ) {
                    return true;
                }
            }
        }
        return false;
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

    protected String getSemester(){ return this.semester;}

    protected boolean sectionOf(Section other) {
        return this.department.equals(other.getDepartment()) && this.courseCode == other.getCourseCode();
    }
}
