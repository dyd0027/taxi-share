package taxi.share.back.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import taxi.share.back.model.Routes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MapServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RedisService redisService;
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
//    @Mock
//    private GeoOperations<String, Object> geoOperations;

    private GeoOperations<String, Object> geoOperations;
//    @InjectMocks
    @Autowired
    private MapService mapService;

    private Routes testRoute;
    @BeforeEach
    public void setup() {
        // Mockito 초기화
        MockitoAnnotations.initMocks(this);

        // 테스트용 경로 객체 생성 (출발지, 도착지 좌표, 경로번호 설정)
        testRoute = new Routes();
        testRoute.setRouteNo(1);
        testRoute.setOriginLongitude(126.924876706923);
        testRoute.setOriginLatitude(37.5251775245928);
        testRoute.setDestinationLongitude(126.934876706923);
        testRoute.setDestinationLatitude(37.5151775245928);

        // RedisTemplate의 opsForGeo()가 geoOperations 모킹된 객체를 반환하도록 설정
        // Mock 환경
        // when(redisTemplate.opsForGeo()).thenReturn(geoOperations);
        // 운영 환경
        geoOperations = redisTemplate.opsForGeo();
    }

    @Test
    public void testRoute() {
        // Set up mocked response from the external API
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Mocked Kakao API Response"));

        // Create test data
        Routes routes = new Routes();
        routes.setOriginLatitude(37.4682787075426);
        routes.setOriginLongitude(127.039136433366);
        routes.setDestinationLatitude(36.9878099898812);
        routes.setDestinationLongitude(127.301187652392);

        // Call the service method
        JsonNode response = mapService.route(routes);

        // Assert that the response is as expected
        assertEquals("Mocked Kakao API Response", response);
    }

    @Test
    public void testCheckRoute() {
        // redisService.getCachedData()의 동작을 미리 정의
        when(redisService.getCachedData("routeCache::6")).thenReturn(new Object()); // 원하는 Mock 결과 설정

        boolean result = mapService.checkRoute("routeCache::6");

        // 결과 검증
        assertEquals(true, result);
    }

    @Test
    void testRouteJoin() {
        // 출발지
        geoOperations.add("route:origin", new Point(126.924876706923, 27.5251775245928), 2);
        geoOperations.add("route:origin", new Point(127.039136433366, 37.4682787075426), 3);
        geoOperations.add("route:origin", new Point(126.924876606923, 37.5251774245928), 4);
        // 도착지
        // 도착지 데이터 추가
        geoOperations.add("route:destination", new Point(126.934876706923, 17.5151775245928), 2);
        geoOperations.add("route:destination", new Point(127.301187652392, 36.9878099898812), 3);
        geoOperations.add("route:destination", new Point(126.933876605923, 37.5141774244928), 4);

        // 실제 테스트 메서드 호출
        int result = mapService.routeJoin(testRoute);

        // 결과 값이 예상과 같은지 검증
        assertEquals(4, result);
    }

    // 가짜 GeoLocation 객체 생성 유틸리티 메서드
    // 가짜 GeoResult 객체 생성 유틸리티 메서드
    private GeoResult<RedisGeoCommands.GeoLocation<Object>> mockGeoResult(String routeNo, double longitude, double latitude) {
        RedisGeoCommands.GeoLocation<Object> geoLocation = new RedisGeoCommands.GeoLocation<>(routeNo, new Point(longitude, latitude));
        // Distance 객체를 생성 (여기서는 0.0km 거리로 설정)
        Distance distance = new Distance(0.0, Metrics.KILOMETERS);
        return new GeoResult<>(geoLocation, distance);
    }

}
