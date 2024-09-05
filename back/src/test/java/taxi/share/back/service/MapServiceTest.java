package taxi.share.back.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import taxi.share.back.model.Routes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MapServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MapService mapService;

    @BeforeEach
    public void setup() {
        // Initialize mocks if necessary
    }

    @Test
    public void testRoute() {
        // Set up mocked response from the external API
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), Mockito.eq(String.class)))
                .thenReturn(ResponseEntity.ok("Mocked Kakao API Response"));

        // Create test data
        Routes routes = new Routes();
        routes.setOriginLatitude(37.4682787075426);
        routes.setOriginLongitude(127.039136433366);
        routes.setDestinationLatitude(36.9878099898812);
        routes.setDestinationLongitude(127.301187652392);

        // Call the service method
        String response = mapService.route(routes);

        // Assert that the response is as expected
        assertEquals("Mocked Kakao API Response", response);
    }
}
