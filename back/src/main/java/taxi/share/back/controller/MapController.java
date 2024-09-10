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
        return ResponseEntity.ok(mapService.route(routeRequestData.getRouteData()));
    }
//
//    // 테스트용
//    // 한명이 아니라 무조건 두명이 경로를 찾을때 연결해줘야함 매칭이 되어서 두명의 정보를 가져올 때(테스트)
//    @PostMapping("/route")
//    public ResponseEntity<String> getRoute(@RequestBody Routes routeData, HttpServletResponse response) {
//
//
//        log.info("Route object: {}", routeData);
//        // 두 번째 사용자의 경로
//        Routes route2 = new Routes();
//        route2.setRouteNO(1);
//        route2.setOriginLatitude(37.4682787075426); // 서초 경도
//        route2.setOriginLongitude(127.039136433366); // 서초 위도
//        route2.setDestinationLatitude(36.9878099898812); // 안성 경도
//        route2.setDestinationLongitude(127.301187652392); // 안성 위도
//
//
//
//        // 하드코딩된 경로를 사용하여 최적의 경로 계산
//        String result = mapService.routeWithWaypoints(routeData, route2);
//
//        return ResponseEntity.ok(result);
//    }
}
