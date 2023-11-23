package rio.money.Test.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("")
public class TestController {

    private SessionRepository sessionRepository;

    @GetMapping("/")
    public ResponseEntity test(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping("/api/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // Simulated authentication - Replace this with your actual authentication logic
        if ("exampleUser".equals(username) && "examplePassword".equals(password)) {
            // Create a new session and store user information
            Session session = sessionRepository.createSession();
            session.setAttribute("username", username);
            sessionRepository.save(session);
            return "Login successful";
        } else {
            return "Login failed";
        }
    }

    @PostMapping("api/logout")
    public String logout(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        sessionRepository.deleteById(sessionId);
        return "Logout successful";
    }
}
