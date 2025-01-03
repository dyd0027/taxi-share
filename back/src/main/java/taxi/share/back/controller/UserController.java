package taxi.share.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
    private final CacheManager cacheManager;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil,CacheManager cacheManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user, HttpServletResponse response) {
        try {
            User dbUser = userService.findUserByUserId(user.getUserId());
            User valiedUser = userService.login(dbUser, user.getUserPassword(), response);
            return ResponseEntity.ok(valiedUser);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(401).body(null);
        }
    }
    @GetMapping("/check-session")
    public ResponseEntity<Boolean> sessionCheck(HttpServletRequest request, HttpServletResponse response,@RequestParam(name = "cookie", required = false) String cookie) {
        String token = jwtUtil.extractTokenFromCookie(request, cookie);
        if (token != null) {
            boolean isValidated = userService.validateToken(token, response);
            if (!isValidated) {
                jwtUtil.invalidateCookie(response, "jwt-token");
            }
            return ResponseEntity.ok(isValidated);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
