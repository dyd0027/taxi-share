package taxi.share.back.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import taxi.share.back.model.Routes;
import taxi.share.back.service.MapService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MapController.class)
public class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MapService mapService;

    @Test
    public void testGetRoute() throws Exception {
        // Mocking service response
        Mockito.when(mapService.route(Mockito.any(Routes.class))).thenReturn("Mocked Kakao API response");

        // Request body
        String requestBody = """
        {
            "originLatitude": 37.4682787075426,
            "originLongitude": 127.039136433366,
            "destinationLatitude": 36.9878099898812,
            "destinationLongitude": 127.301187652392
        }
        """;

        mockMvc.perform(post("/api/map/route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Mocked Kakao API response"));
    }
}
