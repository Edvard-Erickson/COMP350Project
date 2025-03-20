package software.engineering.main;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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
        Database db = new Database();
        db.readFile();
        this.courseList = db.getDataList();

        courseList.removeIf(s -> !s.getSemester().equals("2025_Spring"));

        this.s = new Search(courseList);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String fileName, @RequestParam List<String> courses) {
        ArrayList<Section> timeblocks = toCourseObjects(courses);
        if (timeblocks.isEmpty()) {
            System.out.println("No valid courses found for the given input.");
            ResponseEntity.badRequest().build();
        }
        Schedule schedule = new Schedule();
        for (Section s : timeblocks) {
            schedule.addSection(s);
        }

        File file = schedule.saveToSchedule(fileName);

        try {
            byte[] contentBytes = Files.readAllBytes(file.toPath());
            ByteArrayResource resource = new ByteArrayResource(contentBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".json\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(contentBytes.length)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "[]";
        }

        try {
            // Save the uploaded file to a temporary location
            File tempFile = File.createTempFile("uploaded-", ".json");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }

            // Load the schedule from the saved file
            Schedule schedule = Schedule.loadSchedule(tempFile.getAbsolutePath());

            // Process the schedule as needed
            // For example, print the schedule
            schedule.printSchedule();

            return new Gson().toJson(schedule.getSchedule());
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
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
                        System.out.println(filter.getValue());
                        String[] times = filter.getValue().split(",");
                        HashMap<String, String[]> timeMap = new HashMap<>();
                        for (String time : times) {
                            for (String key : new String[]{"M", "T", "W", "R", "F"}) {
                                String[] timeRange = time.split("-");
                                timeMap.put(key, timeRange);
                            }
                        }
                        s.addFilter(new TimeslotFilter(timeMap));
                    }
                }
            }
        }
        Search filteredSearch = new Search(s.applyFilters());
        System.out.println(new Gson().toJson(filteredSearch.courseSearch(query)));
        return new Gson().toJson(filteredSearch.courseSearch(query));
    }

    @GetMapping("/search")
    public String getEmptySearch(@RequestParam Map<String, String> filters) {
        System.out.println("EMPTY SEARCH");
        s.clearFilters();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (!filter.getValue().isEmpty()) {
                switch (filter.getKey()) {
                    case "department" -> s.addFilter(new DepartmentFilter(filter.getValue()));
                    case "professor" -> s.addFilter(new ProfessorFilter(filter.getValue()));
                    case "times" -> {
                        System.out.println(filter.getValue());
                        String[] times = filter.getValue().split(",");
                        HashMap<String, String[]> timeMap = new HashMap<>();
                        for (String time : times) {
                            for (String key : new String[]{"M", "T", "W", "R", "F"}) {
                                String[] timeRange = time.split("-");
                                timeMap.put(key, timeRange);
                            }
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
        System.out.print("GENERATING COURSES WITH: ");
        for (Section s : timeblocks) {
            System.out.print(s.getCourseName() + " ");
        }
        System.out.println();

        if (timeblocks.isEmpty()) {
            System.out.println("No valid courses found for the given input.");
            return "[]";
        }

        AutoScheduler sg = new AutoScheduler(courseList);
        for (Section s : timeblocks) {
            sg.addToPossibleCourseList(s);
        }
        sg.generatePossibleSchedules();
        ArrayList<Schedule> possible = sg.getSchedulePossibilities();

        if (possible.isEmpty()) {
            System.out.println("No possible schedules generated.");
            return "[]";
        }

        for (Schedule schedule : possible) {
            schedule.printSchedule();
        }

        ArrayList<ArrayList<Timeblock>> scheduleCourses = new ArrayList<>();
        for (Schedule schedule : possible) {
            scheduleCourses.add(schedule.getSchedule());
        }

        return new Gson().toJson(scheduleCourses);
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
            Pattern pattern = Pattern.compile("([A-Z]+)(\\d{3})([A-Z])(\\d{4}_[A-Za-z]+)");
            Matcher matcher = pattern.matcher(course);
            if (matcher.matches()) {
                String department = matcher.group(1);
                int courseCode = Integer.parseInt(matcher.group(2));
                char section = matcher.group(3).charAt(0);
                String semester = matcher.group(4);
                for (Section c : courseList) {
                    if (c.getDepartment().equals(department) && c.getCourseCode() == courseCode && c.getSection() == section && c.getSemester().equals(semester)) {
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