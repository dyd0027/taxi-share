package taxi.share.back.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.share.back.model.User;
import taxi.share.back.service.UserService;
import taxi.share.back.util.JwtUtil;

import javax.servlet.http.HttpServletResponse;

@Slf4j // log
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        log.info(user.getUserId());
        try {
            String token = userService.login(user.getUserId(), user.getUserPassword());
//            jwtUtil.addTokenToCookie(token, response);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }
}
