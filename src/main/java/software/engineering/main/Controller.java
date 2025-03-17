package software.engineering.main;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class Controller {
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

        ArrayList<Section> courseList = new ArrayList<>();
        courseList.add(a);
        courseList.add(b);
        courseList.add(c);

        this.s = new Search(courseList);
    }

    Search search = new Search(new ArrayList<>());

    @GetMapping("/search/{query}")
    public String getSearchResults(@PathVariable String query) {
        System.out.println("searching for: " + query);
        System.out.println(s.courseSearch(query));
        return new Gson().toJson(s.courseSearch(query));
    }
}
