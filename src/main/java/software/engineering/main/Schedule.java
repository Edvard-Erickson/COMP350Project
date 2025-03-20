package software.engineering.main;

import com.google.gson.Gson;

import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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

    public File saveToSchedule(String fileName) {
        File file = new File("src/main/resources/" + fileName + ".json");
        try {
            Gson gson = new Gson().newBuilder().create();
            String json = gson.toJson(this);

            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //load a schedule from resources with the filepath, returns a Schedule object
    protected static Schedule loadSchedule(String filePath) {
        Schedule schedule = new Schedule();
        JSONParser parser = new JSONParser();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath))) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray classesArray = (JSONArray) jsonObject.get("classes");
            for (Object classObj : classesArray) {
                JSONObject classJson = (JSONObject) classObj;

                String department = (String) classJson.get("department");
                String courseName = (String) classJson.get("name");
                long courseCode = (long) classJson.get("courseCode");
                char section = ((String) classJson.get("section")).charAt(0);
                String professor = (String) classJson.get("professor");
                String semester = (String) classJson.get("semester");

                HashMap<String, String[]> times = new HashMap<>();
                JSONObject timesJson = (JSONObject) classJson.get("times");
                for (Object day : timesJson.keySet()) {
                    JSONArray timeArray = (JSONArray) timesJson.get(day);
                    String startTime = (String) timeArray.get(0);
                    String endTime = (String) timeArray.get(1);
                    times.put((String) day, new String[]{startTime, endTime});
                }

                Section sec = new Section(department, (int) courseCode, section, courseName, professor, semester, times);
                schedule.addSection(sec);
            }

            JSONArray blocksArray = (JSONArray) jsonObject.get("blocks");
            for (Object blockObj : blocksArray) {
                JSONObject blockJson = (JSONObject) blockObj;

                HashMap<String, String[]> times = new HashMap<>();
                JSONObject timesJson = (JSONObject) blockJson.get("times");
                for (Object day : timesJson.keySet()) {
                    JSONArray timeArray = (JSONArray) timesJson.get(day);
                    String startTime = (String) timeArray.get(0);
                    String endTime = (String) timeArray.get(1);
                    times.put((String) day, new String[]{startTime, endTime});
                }

                Timeblock timeblock = new Timeblock(times);
                schedule.addTimeblock(timeblock);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return schedule;
    }
}
