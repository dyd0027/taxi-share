package taxi.share.back.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.share.back.model.Routes;
import taxi.share.back.service.MapService;
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/map")
public class MapController {
    private final MapService mapService;

    @PostMapping("/route")
    public ResponseEntity<Routes> getRoute(@RequestBody Routes routeData, HttpServletResponse response) {
        log.info("Route object: {}", routeData);
        mapService.route(routeData);
        return ResponseEntity.ok(routeData);
    }
}
