package software.engineering.main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Database {

    private ArrayList<Section> dataList;

    public Database() {
        dataList = new ArrayList<>();
    }

    // Hardcoded file path for the JSON file located in the resources folder
    public void readFile() {
        String filePath = "data_wolfe.json"; // This should be the name of your JSON file in the resources folder
        JSONParser parser = new JSONParser();

        try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath))) {
            if (reader == null) {
                throw new IOException("File not found: " + filePath);
            }

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray classesArray = (JSONArray) jsonObject.get("classes");

            for (Object obj : classesArray) {
                JSONObject classObj = (JSONObject) obj;

                String department = (String) classObj.get("subject");
                String courseName = (String) classObj.get("name");
                long courseCode = (long) classObj.get("number");
                char section = ((String) classObj.get("section")).charAt(0);

                JSONArray facultyArray = (JSONArray) classObj.get("faculty");
                String professor = (facultyArray != null && !facultyArray.isEmpty()) ? (String) facultyArray.get(0) : "Unknown";

                // Extract times data
                HashMap<String, String[]> times = new HashMap<>();
                JSONArray timesArray = (JSONArray) classObj.get("times");
                if (timesArray != null) {
                    for (Object timeObj : timesArray) {
                        JSONObject timeBlock = (JSONObject) timeObj;
                        String day = (String) timeBlock.get("day");
                        String startTime = (String) timeBlock.get("start_time");
                        String endTime = (String) timeBlock.get("end_time");
                        times.put(day, new String[]{startTime, endTime});
                    }
                }

                Section sec = new Section(department, (int) courseCode, section, courseName, professor, times);
                dataList.add(sec);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //get method for datalist
    public ArrayList<Section> getDataList() {
        return dataList;
    }

    //more for testing purposes, it will print out all relevant data, times are printed for mondays
    //print data method
    public void printData() {
        for (Section section : dataList) {
            System.out.println(section.getDepartment() + " "
                    + section.getCourseCode() + " "
                    + section.getSection() + " | "
                    + section.getCourseName() + " | "
                    + section.getProfessor() + " | "
                    + "M: " + (section.getTimes().get("M") != null ? Arrays.toString(section.getTimes().get("M")) : "no times on monday") + " | "
                    + "T: " + (section.getTimes().get("T") != null ? Arrays.toString(section.getTimes().get("T")) : "no times on tuesday") + " | "
                    + "W: " + (section.getTimes().get("W") != null ? Arrays.toString(section.getTimes().get("W")) : "no times on wednesday") + " | "
                    + "R: " + (section.getTimes().get("R") != null ? Arrays.toString(section.getTimes().get("R")) : "no times on thursday") + " | "
                    + "F: " + (section.getTimes().get("F") != null ? Arrays.toString(section.getTimes().get("F")) : "no times on friday"));
            System.out.println();
        }
    }
}
