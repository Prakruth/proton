package rio.money.Test.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rio.money.Test.User;
import rio.money.Test.models.ApiResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static final Map<String, User> userMap = new HashMap<>();
    private static final Map<String, List<String>> unreadMessages = new HashMap<>();


    @PostMapping("/create/user")
    public ResponseEntity createUser(@RequestBody User user) {
        String username = user.getUsername();

        if (userMap.containsKey(username)) {
            return new ResponseEntity(new ApiResponse("failure", "User with username '" + username + "' already exists."), HttpStatus.OK);
        }

        userMap.put(username, user);
        return new ResponseEntity (new ApiResponse("success", "User created successfully: " + user.getUsername()),
        HttpStatus.OK);

    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (userMap.containsKey(username) && userMap.get(username).getPassword().equals(password)) {

            return new ApiResponse("success", null);
        } else {
            return new ApiResponse("failure", "Invalid username or password.");
        }
    }

    @GetMapping("/get")
    public ApiResponse getUsers() {
        List<String> usernames = new ArrayList<>(userMap.keySet());
        return new ApiResponse("success", usernames);
    }

    @GetMapping("/get/unread")
    public ApiResponse getUnreadMessages(@RequestBody Map<String, String> requestBody, HttpSession httpSession) {
        String loggedInUser = (String) httpSession.getAttribute("username");

        if (loggedInUser == null || !loggedInUser.equals(requestBody.get("username"))) {
            return new ApiResponse("failure", "User not logged in or invalid username.");
        }

        String username = loggedInUser;

        if (unreadMessages.containsKey(username) && !unreadMessages.get(username).isEmpty()) {
            List<String> messages = unreadMessages.get(username);
            unreadMessages.put(username, new ArrayList<>()); // Mark messages as read
            return new ApiResponse("success", messages);
        } else {
            return new ApiResponse("success", "No new messages");
        }
    }

    @PostMapping("/send/text/user")
    public ApiResponse sendTextToUser(@RequestBody Map<String, String> requestBody) {
        String fromUser = requestBody.get("from");
        String toUser = requestBody.get("to");
        String text = requestBody.get("text");

        if (!userMap.containsKey(fromUser) || !userMap.containsKey(toUser)) {
            return new ApiResponse("failure", "Invalid user(s).");
        }

        // Simulate sending the text message
        if (!unreadMessages.containsKey(toUser)) {
            unreadMessages.put(toUser, new ArrayList<>());
        }
        unreadMessages.get(toUser).add(fromUser + ": " + text);

        return new ApiResponse("success", null);
    }

    @GetMapping("/get/history")
    public ApiResponse getChatHistory(@RequestBody Map<String, String> requestBody) {
        String user = requestBody.get("user");
        String friend = requestBody.get("friend");

        if (!userMap.containsKey(user) || !userMap.containsKey(friend)) {
            return new ApiResponse("failure", "Invalid user(s).");
        }

        List<String> chatHistory = new ArrayList<>();
        if (unreadMessages.containsKey(user)) {
            for (String message : unreadMessages.get(user)) {
                if (message.startsWith(friend + ":") || message.startsWith(user + ":")) {
                    chatHistory.add(message);
                }
            }
        }

        return new ApiResponse("success", chatHistory);
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestBody Map<String, String> requestBody, HttpSession httpSession) {
        String username = requestBody.get("username");

        if (!userMap.containsKey(username)) {
            return new ApiResponse("failure", "Invalid user.");
        }

        // Invalidate the session
        httpSession.invalidate();

        return new ApiResponse("success", null);
    }


}
