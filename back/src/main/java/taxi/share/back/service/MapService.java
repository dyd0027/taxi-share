package taxi.share.back.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import taxi.share.back.model.Routes;
import taxi.share.back.repository.RoutesRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {
    @Value("${kakao.rest.api.key}")
    private String kakaoApiKey;
    @Value("${kakao.rest.api.url}")
    private String kakaoApiURL;
    private final RestTemplate restTemplate;
    private final RoutesRepository routesRepository;
    private final CacheManager cacheManager;
    private final RedisService redisService;
    public String route(Routes routes){

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
                    newRoutes = registerRoute(routes);
                }
            }

            if (rootNode instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) rootNode;

                // 새로운 필드 추가 ("routeNo" : 원하는 숫자)
                objectNode.put("routeNo", newRoutes.getRouteNo());  // 원하는 숫자를 여기에 넣음

                // 변경된 JSON 객체를 다시 문자열로 변환
                jsonResponse = objectMapper.writeValueAsString(objectNode);
                System.out.println("Updated JSON: " + jsonResponse);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    public Routes registerRoute(Routes routes) throws Exception {
        try {
            // 먼저 엔티티를 저장하여 routeNo가 생성되도록 합니다.
            Routes savedRoute = routesRepository.save(routes);

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

    public String routeJoin(Routes route) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Content-Type", "application/json");

        // 출발지, 경유지 및 목적지 설정 (하드코딩)
        String origin = route.getOriginLongitude() + "," + route.getOriginLatitude();
        String destination = route.getOriginLongitude() + "," + route.getOriginLatitude(); // 첫 번째 사용자의 목적지
        findNearRoute(route);

        return "return";
    }

    public void findNearRoute(Routes route){

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
