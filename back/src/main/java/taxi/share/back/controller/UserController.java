package taxi.share.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.share.back.model.User;
import taxi.share.back.service.UserService;
import taxi.share.back.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user, HttpServletResponse response) {
        log.info("Login attempt for user: {}", user.getUserId());
        try {
            User valiedUser = userService.login(user.getUserId(), user.getUserPassword());
            jwtUtil.addTokenToCookie(jwtUtil.generateToken(valiedUser.getUserId()), response);
            return ResponseEntity.ok(valiedUser);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(401).body(null);
        }
    }
    @GetMapping("/check-session")
    public ResponseEntity<Boolean> sessionCheck(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.extractTokenFromCookie(request);
        try {
            if (token != null) {
                String userId = jwtUtil.extractUserId(token);
                User user = userService.findUserByUserId(userId);
                return ResponseEntity.ok(jwtUtil.validateToken(token, user));
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }
}
