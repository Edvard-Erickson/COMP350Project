package software.engineering.main;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class Controller {
    ArrayList<Section> courseList;
    Search s;
    public Controller() {
        HashMap<String, String[]> atimes = new HashMap<>();
        atimes.put("M", new String[]{"10:00", "10:50"});
        atimes.put("W", new String[]{"10:00", "10:50"});
        atimes.put("F", new String[]{"10:00", "10:50"});
        Section a = new Section("COMP", 101, 'A', "Intro to CS", "John", atimes);

        HashMap<String, String[]> btimes = new HashMap<>();
        btimes.put("M", new String[]{"10:00", "10:50"});
        btimes.put("T", new String[]{"9:30", "10:20"});
        btimes.put("W", new String[]{"10:00", "10:50"});
        btimes.put("F", new String[]{"10:00", "10:50"});
        Section b = new Section("MATH", 101, 'A', "Calculus 1", "John", btimes);

        HashMap<String, String[]> ctimes = new HashMap<>();
        ctimes.put("M", new String[]{"11:00", "11:50"});
        ctimes.put("T", new String[]{"9:30", "10:20"});
        ctimes.put("W", new String[]{"11:00", "11:50"});
        ctimes.put("F", new String[]{"11:00", "11:50"});
        Section c = new Section("MATH", 102, 'A', "Calculus 2", "Alex", ctimes);

        courseList = new ArrayList<>();
        courseList.add(a);
        courseList.add(b);
        courseList.add(c);

        this.s = new Search(courseList);
    }

    @GetMapping("/search/{query}")
    public String getSearchResults(@PathVariable String query, @RequestParam Map<String, String> filters) {
        s.clearFilters();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (!filter.getValue().isEmpty()) {
                switch (filter.getKey()) {
                    case "department" -> s.addFilter(new DepartmentFilter(filter.getValue()));
                    case "professor" -> s.addFilter(new ProfessorFilter(filter.getValue()));
                    case "times" -> {
                        String[] times = filter.getValue().split(",");
                        HashMap<String, String[]> timeMap = new HashMap<>();
                        for (String time : times) {
                            String day = time.substring(0, 1);
                            String[] timeRange = time.substring(1).split("-");
                            timeMap.put(day, timeRange);
                        }
                        s.addFilter(new TimeslotFilter(timeMap));
                    }
                }
            }
        }
        Search filteredSearch = new Search(s.applyFilters());
        return new Gson().toJson(filteredSearch.courseSearch(query));
    }

    @GetMapping("/search")
    public String getEmptySearch(@RequestParam Map<String, String> filters) {
        s.clearFilters();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (!filter.getValue().isEmpty()) {
                switch (filter.getKey()) {
                    case "department" -> s.addFilter(new DepartmentFilter(filter.getValue()));
                    case "professor" -> s.addFilter(new ProfessorFilter(filter.getValue()));
                    case "times" -> {
                        String[] times = filter.getValue().split(",");
                        HashMap<String, String[]> timeMap = new HashMap<>();
                        for (String time : times) {
                            String day = time.substring(0, 1);
                            String[] timeRange = time.substring(1).split("-");
                            timeMap.put(day, timeRange);
                        }
                        s.addFilter(new TimeslotFilter(timeMap));
                    }
                }
            }
        }
        Search filteredSearch = new Search(s.applyFilters());
        return new Gson().toJson(filteredSearch.courseSearch(""));
    }

    @GetMapping("/generate")
    public String generateSchedule(@RequestParam List<String> courses) {
        ArrayList<Section> timeblocks = toCourseObjects(courses);
        AutoScheduler sg = new AutoScheduler();
        for (Section timeblock : timeblocks) {
            sg.addToCourseList(timeblock);
        }
        sg.generatePossibleSchedules();
        return new Gson().toJson(sg.getSchedulePossibilties());
    }

    @GetMapping("/isConflict")
    public boolean isConflict(@RequestParam List<String> courses) {
        System.out.println("checking for conflict: " + courses);
        ArrayList<Section> timeblocks = toCourseObjects(courses);
        for (Timeblock t : timeblocks) {
            for (Timeblock t2 : timeblocks) {
                if (t != t2 && t.conflictsWith(t2)) {
                    System.out.println("FOUND CONFLICT");
                    return true;
                }
            }
        }

        System.out.println("NO CONFLICT");
        return false;
    }

    @GetMapping("/coursesInfo")
    public String coursesInfo(@RequestParam List<String> courses) {
        ArrayList<Section> c = toCourseObjects(courses);
        return new Gson().toJson(c);
    }

    public ArrayList<Section> toCourseObjects(List<String> courses) {
        ArrayList<Section> timeblocks = new ArrayList<>();
        for (String course : courses) {
            Pattern pattern = Pattern.compile("([A-Z]+)(\\d{3})([A-Z])");
            Matcher matcher = pattern.matcher(course);
            if (matcher.matches()) {
                String department = matcher.group(1);
                int courseCode = Integer.parseInt(matcher.group(2));
                char section = matcher.group(3).charAt(0);
                for (Section c : courseList) {
                    if (c.getDepartment().equals(department) && c.getCourseCode() == courseCode && c.getSection() == section) {
                        timeblocks.add(c);
                        System.out.println("COURSE: " + c.getCourseName() + " " + c.getSection());
                        for (String day : c.getTimes().keySet()) {
                            System.out.println("   " + day + " " + c.getTimes().get(day)[0] + " - " + c.getTimes().get(day)[1]);
                        }
                    }
                }
            }
        }
        return timeblocks;
    }
}