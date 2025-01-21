package taxi.share.back.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import taxi.share.back.model.Routes;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {
    @Value("${kakao.rest.api.key}")
    private String kakaoApiKey;
    @Value("${kakao.rest.api.url}")
    private String kakaoApiURL;
    private final RestTemplate restTemplate;
    private final RouteService routeService;
    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;
    private final MatchingService matchingService;
    public JsonNode route(Routes routes){

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Content-Type", "application/json");

        // 쿼리 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoApiURL)
                .queryParam("origin", routes.getOriginLongitude() + "," + routes.getOriginLatitude())
                .queryParam("destination", routes.getDestinationLongitude() + "," + routes.getDestinationLatitude());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        String jsonResponse = response.getBody();
        log.info("Kakao API Response: {}", jsonResponse);

        try {
            // ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열을 JsonNode로 파싱
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            // 원하는 값 추출 (예: distance, duration 등)
            JsonNode routesNode = rootNode.path("routes");
            Routes newRoutes = new Routes();
            if (routesNode.isArray()) {
                for (JsonNode route : routesNode) {
                    // Summary 정보 추출
                    JsonNode summary = route.path("summary");
                    int distance = summary.path("distance").asInt();
                    JsonNode fare = summary.path("fare");
                    int taxi = fare.path("taxi").asInt();
                    int toll = fare.path("toll").asInt();

                    routes.setDistanceM(distance);
                    routes.setFare(taxi);
                    routes.setToll(toll);
//                    newRoutes = registerRoute(routes);
                }
            }
            ObjectNode objectNode = null;
            if (rootNode instanceof ObjectNode) {
                objectNode = (ObjectNode) rootNode;

                // 새로운 필드 추가 ("routeObj" : route)
                objectNode.set("routeObj", objectMapper.valueToTree(routes));
            }
            return objectNode;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Routes registerRoute(Routes routes) throws Exception {
        try {
            // 먼저 엔티티를 저장하여 routeNo가 생성되도록 합니다.
            Routes savedRoute = routeService.save(routes);

            // 저장된 routeNo를 캐시에 넣습니다.
            if (savedRoute != null) {
                cacheManager.getCache("routeCache").put(savedRoute.getRouteNo(), savedRoute);
            }

            return savedRoute;
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Data integrity violation during route save", e);
        } catch (Exception e) {
            throw new Exception("Error occurred while saving the route", e);
        }
    }

    public boolean checkRoute(String key){
        Routes routes1 = cacheManager.getCache("routeCache").get("15",Routes.class);
        Routes routes = (Routes) redisService.getCachedData(key);
        return true;
    }

    public Integer routeJoin(Routes routes){
        try{
            // 루트 저장
            Routes route = registerRoute(routes);

            // 1. 사용자의 위치를 저장 (출발지와 도착지)
            saveUserLocation(route);

            // 2. 출발지에서 반경 300m 이내의 사용자 검색
            GeoResults<RedisGeoCommands.GeoLocation<Object>> nearbyOrigins = findNearbyOrigins(route.getOriginLongitude(), route.getOriginLatitude(), 300);

            // 3. 도착지에서 반경 300m 이내의 사용자 검색
            GeoResults<RedisGeoCommands.GeoLocation<Object>> nearbyDestinations = findNearbyDestinations(route.getDestinationLongitude(), route.getDestinationLatitude(), 300);

            // Iterate over nearbyOrigins
            for (GeoResult<RedisGeoCommands.GeoLocation<Object>> originResult : nearbyOrigins) {
                RedisGeoCommands.GeoLocation<Object> originLocation = originResult.getContent();
                int originName = (int) originLocation.getName();

                // Exclude specific routeNo
                if (originName == route.getRouteNo()) {
                    continue;
                }

                // Compare with nearbyDestinations
                for (GeoResult<RedisGeoCommands.GeoLocation<Object>> destinationResult : nearbyDestinations) {
                    RedisGeoCommands.GeoLocation<Object> destinationLocation = destinationResult.getContent();
                    int destinationName = (int) destinationLocation.getName();

                    if (originName == destinationName) {
                        // Match found, return the matching location
                        Routes routesA = routeService.findByRoutesByRouteNo(originName);
                        Routes routesB = routeService.findByRoutesByRouteNo(route.getRouteNo());
                        matchingService.notifyMatchingSuccess(routesA.getRtUserNo(),routesB.getRtUserNo());
                        return originName;
                    }
                }
            }
            return 0;
        }catch (Exception e){
            return -1;
        }
    }

    public void saveUserLocation(Routes route) {
        redisTemplate.opsForGeo().add("route:origin", new Point(route.getOriginLongitude(), route.getOriginLatitude()), route.getRouteNo());
        redisTemplate.opsForGeo().add("route:destination", new Point(route.getDestinationLongitude(), route.getDestinationLatitude()), route.getRouteNo());
    }

    // 출발지에서 radiusInMeters 이내 유저 검색
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> findNearbyOrigins(double longitude, double latitude, double radiusInMeters) {
        Circle searchArea = new Circle(new Point(longitude, latitude), new Distance(radiusInMeters, Metrics.METERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()   // 거리 정보를 포함하여 검색 결과에 포함
                .sortAscending();    // 거리 기준으로 오름차순 정렬

        return redisTemplate.opsForGeo().radius("route:origin", searchArea, args);
    }

    // 도착지에서 radiusInMeters 이내 유저 검색
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> findNearbyDestinations(double longitude, double latitude, double radiusInMeters) {
        Circle searchArea = new Circle(new Point(longitude, latitude), new Distance(radiusInMeters, Metrics.METERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()   // 거리 정보를 포함하여 검색 결과에 포함
                .sortAscending();    // 거리 기준으로 오름차순 정렬

        return redisTemplate.opsForGeo().radius("route:destination", searchArea, args);
    }

    public Optional<Object> findMatchingUser(double originLongitude, double originLatitude,
                                             double destinationLongitude, double destinationLatitude,
                                             double radiusInMeters) {
        // 1. 출발지에서 반경 내 사용자 검색
        Circle originSearchArea = new Circle(new Point(originLongitude, originLatitude), new Distance(radiusInMeters, Metrics.METERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .sortAscending(); // 가까운 순으로 정렬

        GeoResults<RedisGeoCommands.GeoLocation<Object>> nearbyOrigins = redisTemplate.opsForGeo().radius("route:origin", originSearchArea, args);

        if (nearbyOrigins != null) {
            // 2. 검색된 사용자 중 도착지 조건 검증
            for (GeoResult<RedisGeoCommands.GeoLocation<Object>> originResult : nearbyOrigins) {
                Object userId = originResult.getContent().getName();

                // 2-1. 사용자 도착지의 좌표를 Redis에서 가져옴
                List<Point> destinationPoints = redisTemplate.opsForGeo().position("route:destination", userId);
                if (destinationPoints != null && !destinationPoints.isEmpty()) {
                    Point userDestinationPoint = destinationPoints.get(0);

                    // 2-2. 도착지 반경 검증
                    Distance destinationDistance = redisTemplate.opsForGeo().distance(
                            "route:destination",
                            new Point(destinationLongitude, destinationLatitude),
                            userDestinationPoint
                    );

                    if (destinationDistance != null && destinationDistance.getValue() <= radiusInMeters) {
                        // 조건을 만족하는 첫 번째 사용자 반환
                        return Optional.of(userId);
                    }
                }
            }
        }

        // 조건을 만족하는 사용자가 없는 경우
        return Optional.empty();
    }

//    public String routeWithWaypoints(Routes route) {
//        // 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
//        headers.set("Content-Type", "application/json");
//
//        // 출발지, 경유지 및 목적지 설정 (하드코딩)
//        String origin = route1.getOriginLongitude() + "," + route1.getOriginLatitude();
//        String waypoint1 = route2.getOriginLongitude() + "," + route2.getOriginLatitude(); // 두 번째 사용자의 출발지
//        String waypoint2 = route2.getDestinationLongitude() + "," + route2.getDestinationLatitude(); //  두 번째 사용자의 목적지
//        String destination = route1.getOriginLongitude() + "," + route1.getOriginLatitude(); // 첫 번째 사용자의 목적지
//
//        // 경유지 문자열 생성
//        // String waypoints = waypoint1 + "|" + waypoint2;
//
//
//        // 경유지 문자열 생성 (단일 경유지로 테스트)
//        String waypoints = waypoint1;
//        String waypoints2 = waypoint2;
//
//        // 쿼리 파라미터 설정
//        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoApiURL)
//                .queryParam("origin", origin)
//                .queryParam("destination", destination)
//                .queryParam("waypoints", waypoints)
//                .queryParam("waypoints", waypoints2);
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        // API 호출
//        ResponseEntity<String> response = restTemplate.exchange(
//                uriBuilder.toUriString(),
//                HttpMethod.GET,
//                entity,
//                String.class
//        );
//
//        log.info("Kakao API Response: {}", response.getBody());
//
//        return response.getBody();
//    }

}
