package software.engineering.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;

public class main {
    Student student;
    Filter filter;
    Search search;

    protected static void run(){

    }

    public static void main(String[] args) {
        System.out.println("hello");
        System.out.println("Eddie is here");

        Database db = new Database();
        db.readFile();

        // for Christopher's testing purposes
        HashMap<String, String[]> atimes = new HashMap<>();
        atimes.put("M", new String[]{"10:00:00", "10:50:00"});
        atimes.put("W", new String[]{"10:00:00", "10:50:00"});
        atimes.put("F", new String[]{"10:00:00", "10:50:00"});
        Section a = new Section("COMP", 101, 'A', "Intro to CS", "John", "",atimes);

        HashMap<String, String[]> btimes = new HashMap<>();
        btimes.put("M", new String[]{"10:00:00", "10:50:00"});
        btimes.put("T", new String[]{"9:30:00", "10:20:00"});
        btimes.put("W", new String[]{"10:00:00", "10:50:00"});
        btimes.put("F", new String[]{"10:00:00", "10:50:00"});
        Section b = new Section("MATH", 101, 'A', "Calculus 1", "John", "",btimes);

        HashMap<String, String[]> ctimes = new HashMap<>();
        ctimes.put("M", new String[]{"11:00:00", "11:50:00"});
        ctimes.put("T", new String[]{"9:30:00", "10:20:00"});
        ctimes.put("W", new String[]{"11:00:00", "11:50:00"});
        ctimes.put("F", new String[]{"11:00:00", "11:50:00"});
        Section c = new Section("MATH", 102, 'A', "Calculus 2", "Alex", "",ctimes);

        HashMap<String, String[]> dtimes = new HashMap<>();
        dtimes.put("M", new String[]{"1:00:00", "1:50:00"});
        dtimes.put("W", new String[]{"1:00:00", "1:50:00"});
        dtimes.put("F", new String[]{"1:00:00", "1:50:00"});
        Section d = new Section("MATH", 238, 'A', "Differential Equations", "Alex", "",dtimes);

        ArrayList<Section> courseList = new ArrayList<>();
        courseList.add(a);
        courseList.add(b);
        courseList.add(c);
        courseList.add(d);

        Search s = new Search(courseList);

        // testing Search
        System.out.println(courseList);
        System.out.println(s.courseSearch("MATH"));
        System.out.println(s.courseSearch("Calculus"));

        System.out.println();

        // testing professorFilter
        ProfessorFilter pf = new ProfessorFilter("John");
        s.addFilter(pf);
        System.out.println(s.applyFilters());
        s.clearFilters();

        // testing departmentFilter
        DepartmentFilter df = new DepartmentFilter("MATH");
        s.addFilter(df);
        System.out.println(s.applyFilters());
        s.clearFilters();

        // testing timeslotFilter
        HashMap<String, String[]> tftimes = new HashMap<>();
        tftimes.put("T", new String[]{"9:30:00", "10:20:00"});
        TimeslotFilter tf = new TimeslotFilter(tftimes);
        s.addFilter(tf);
        System.out.println(s.applyFilters());
        s.clearFilters();

        // testing multipleFilters
        s.addFilter(pf);
        s.addFilter(tf);
        System.out.println(s.applyFilters());
        s.clearFilters();

        // testing conflictsWith
        System.out.println(a.conflictsWith(b));
        System.out.println(b.conflictsWith(c));
        System.out.println(a.conflictsWith(c));
        System.out.println(a.conflictsWith(d));
        System.out.println(c.conflictsWith(d));

        Gson gson = new Gson();

        System.out.println(gson.toJson(a));
        Schedule scheduleA = new Schedule();
        scheduleA.addSection(c);
        scheduleA.addSection(d);

        scheduleA.saveToSchedule("ScheduleA");
    }
}
