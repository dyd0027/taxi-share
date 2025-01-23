package taxi.share.back.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MapServiceIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MapService mapService;

    @BeforeEach
    public void setup() {
        // Initialize mocks if necessary
    }

}
