package taxi.share.back.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final RestTemplate restTemplate;
    private final RoutesRepository routesRepository;

    public String route(Routes routes){
        String url = "https://apis-navi.kakaomobility.com/v1/directions";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Content-Type", "application/json");

        // 쿼리 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
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

        log.info("Kakao API Response: {}", response.getBody());

        return response.getBody();
    }

    // test용
    public String routeWithWaypoints(Routes route1, Routes route2) {
        String url = "https://apis-navi.kakaomobility.com/v1/directions";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Content-Type", "application/json");

        // 출발지, 경유지 및 목적지 설정 (하드코딩)
        String origin = route1.getOriginLongitude() + "," + route1.getOriginLatitude();
        String waypoint1 = route2.getOriginLongitude() + "," + route2.getOriginLatitude(); // 두 번째 사용자의 출발지
        String waypoint2 = route2.getDestinationLongitude() + "," + route2.getDestinationLatitude(); //  두 번째 사용자의 목적지
        String destination = route1.getOriginLongitude() + "," + route1.getOriginLatitude(); // 첫 번째 사용자의 목적지

        // 경유지 문자열 생성
        // String waypoints = waypoint1 + "|" + waypoint2;


        // 경유지 문자열 생성 (단일 경유지로 테스트)
        String waypoints = waypoint1;
        String waypoints2 = waypoint2;

        // 쿼리 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("waypoints", waypoints)
                .queryParam("waypoints", waypoints2);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        log.info("Kakao API Response: {}", response.getBody());

        return response.getBody();
    }



}
