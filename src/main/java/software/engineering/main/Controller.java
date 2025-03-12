package software.engineering.main;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class Controller {
    public Controller() {}

    @GetMapping("/data")
    public String getData() {
        System.out.println("A new hand has touched the beacon. ");
        return "test";
    }
}
