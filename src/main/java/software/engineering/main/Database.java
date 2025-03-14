package software.engineering.main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
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

                // Extract start and end times (default to 00:00 if not available)
                LocalTime startTime = LocalTime.MIN;
                LocalTime endTime = LocalTime.MIN;

                JSONArray timesArray = (JSONArray) classObj.get("times");
                if (timesArray != null && !timesArray.isEmpty()) {
                    JSONObject firstTimeBlock = (JSONObject) timesArray.get(0); // Get first time slot
                    startTime = LocalTime.parse((String) firstTimeBlock.get("start_time"));
                    endTime = LocalTime.parse((String) firstTimeBlock.get("end_time"));
                }

                Section sec = new Section(department, (int) courseCode, section, courseName, professor, startTime, endTime);
                dataList.add(sec);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void printData() {
        for (Section section : dataList) {
            System.out.println(section.getDepartment() + " " + section.getCourseCode() + " " + section.getSection() + " | " + section.getCourseName() + " | " + section.getProfessor());
            System.out.println();
        }
    }
}
