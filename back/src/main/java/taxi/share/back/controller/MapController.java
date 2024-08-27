package taxi.share.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.share.back.model.Routes;
import taxi.share.back.service.UserService;
import taxi.share.back.util.JwtUtil;

@Slf4j
@RestController
@RequestMapping("/api/map")
public class MapController {

    //    private final UserService userService;


    @PostMapping("/route")
    public ResponseEntity<Routes> getRoute(@RequestBody Routes routeData, HttpServletResponse response) {
        log.info("Route object: {}", routeData);
        // 여기에 로직 추가
        return ResponseEntity.ok(routeData);
    }
}
