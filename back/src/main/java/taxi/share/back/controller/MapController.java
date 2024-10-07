package taxi.share.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.share.back.model.Routes;
import taxi.share.back.model.User;
import taxi.share.back.service.MapService;
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/map")
public class MapController {
    private final MapService mapService;

    @Getter
    public static class RouteRequest {
        private Routes routeData;
        private int userNo;
    }

    @PostMapping("/route")
    public ResponseEntity<String> getRoute(@RequestBody RouteRequest routeRequestData, HttpServletRequest request, HttpServletResponse response) {
        log.info("Route object: {}", routeRequestData.getRouteData());
        log.info("UserNo: {}", routeRequestData.getUserNo());
        routeRequestData.getRouteData().setRtUserNo(routeRequestData.getUserNo());
        return ResponseEntity.ok(mapService.route(routeRequestData.getRouteData()));
    }

    @PostMapping("/join")
    public ResponseEntity<String> getRoute(@RequestBody Routes routeData, HttpServletResponse response) {

        String result = mapService.routeJoin(routeData);
        return ResponseEntity.ok(result);
    }
}
