package software.engineering.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
    private ArrayList<Section> classes;
    private ArrayList<Timeblock> blocks;

    protected Schedule() {
        classes = new ArrayList<Section>();
        blocks = new ArrayList<Timeblock>();
    }
    protected Schedule(Schedule schedule){
        this.classes = new ArrayList<>(schedule.classes);
        this.blocks = new ArrayList<>(schedule.blocks);

    }

    protected int addSection(Section section) {
        if(!hasConflict(section)){
            classes.add(section);
            return 0;
        }else{
            System.out.println("Error: Section conflicts with another section");
            return 1; //error Courses conflict
        }
    }

    protected int addTimeblock(Timeblock block) {
        if(!hasConflict(block)) {
            blocks.add(block);
            return 0;
        }else{
            System.out.println("Error: Timeblock conflicts with another timeblock");
            return 1; //error Courses conflict
        }
    }

    protected void removeSection(Section section) {
        classes.remove(section);
    }

    protected void removeTimeblock(Timeblock block) {
        blocks.remove(block);
    }

    protected ArrayList<Timeblock> getSchedule() {
        ArrayList<Timeblock> toReturn = new ArrayList<Timeblock>();
        toReturn.addAll(blocks);
        toReturn.addAll(classes);
        return toReturn;
    }

    //May never get used more than testing
    protected void printSchedule() {
        System.out.println("--- Schedule ---");
        for (Timeblock block : blocks) {
            System.out.println(block.getName());
        }
        for (Section section : classes) {
            System.out.println(section.getCourseName());
        }

    }

    //this hasConflict() allows one to add the same course multiple sections to the schdeule
    private boolean hasConflict(Timeblock course) {
        for (Timeblock scheduledCourse : getSchedule()) {
            //check to see if there is a course at that time
            if (course.conflictsWith(scheduledCourse)) {
                return true;
            }
        }
        return false;
    }

    protected static ArrayList<Schedule> loadSchedules(String filePath) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (InputStreamReader reader = new InputStreamReader(Schedule.class.getClassLoader().getResourceAsStream(filePath))) {
            if (reader == null) {
                throw new IOException("File not found: " + filePath);
            }

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray schedulesArray = (JSONArray) jsonObject.get("schedules");

            for (Object obj : schedulesArray) {
                JSONObject scheduleObj = (JSONObject) obj;
                Schedule schedule = new Schedule();

                JSONArray sectionsArray = (JSONArray) scheduleObj.get("sections");
                for (Object sectionObj : sectionsArray) {
                    JSONObject sectionJson = (JSONObject) sectionObj;

                    String department = (String) sectionJson.get("subject");
                    String courseName = (String) sectionJson.get("name");
                    long courseCode = (long) sectionJson.get("number");
                    char section = ((String) sectionJson.get("section")).charAt(0);
                    String professor = (String) sectionJson.get("professor");
                    String semester = (String) sectionJson.get("semester");

                    HashMap<String, String[]> times = new HashMap<>();
                    JSONArray timesArray = (JSONArray) sectionJson.get("times");
                    if (timesArray != null) {
                        for (Object timeObj : timesArray) {
                            JSONObject timeBlock = (JSONObject) timeObj;
                            String day = (String) timeBlock.get("day");
                            String startTime = (String) timeBlock.get("start_time");
                            String endTime = (String) timeBlock.get("end_time");
                            times.put(day, new String[]{startTime, endTime});
                        }
                    }

                    Section sec = new Section(department, (int) courseCode, section, courseName, professor, semester, times);
                    schedule.addSection(sec);
                }

                JSONArray timeblocksArray = (JSONArray) scheduleObj.get("timeblocks");
                for (Object timeblockObj : timeblocksArray) {
                    JSONObject timeblockJson = (JSONObject) timeblockObj;

                    HashMap<String, String[]> times = new HashMap<>();
                    JSONArray timesArray = (JSONArray) timeblockJson.get("times");
                    if (timesArray != null) {
                        for (Object timeObj : timesArray) {
                            JSONObject timeBlock = (JSONObject) timeObj;
                            String day = (String) timeBlock.get("day");
                            String startTime = (String) timeBlock.get("start_time");
                            String endTime = (String) timeBlock.get("end_time");
                            times.put(day, new String[]{startTime, endTime});
                        }
                    }

                    Timeblock timeblock = new Timeblock(times);
                    schedule.addTimeblock(timeblock);
                }

                schedules.add(schedule);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return schedules;
    }
}
