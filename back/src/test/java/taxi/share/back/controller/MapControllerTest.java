package taxi.share.back.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import taxi.share.back.controller.MapController;
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
    @WithMockUser(username = "testuser", roles = {"USER"})  // 가상의 인증 추가
    public void testGetRoute() throws Exception {
        // Mocking service response
        Mockito.when(mapService.route(Mockito.any(Routes.class))).thenReturn(null);

        // Request body
        String requestBody = """
        {
            "origin": "여의도",
            "originLatitude": 37.4682787075426,
            "originLongitude": 127.039136433366,
            "destination": "노량진",
            "destinationLatitude": 36.9878099898812,
            "destinationLongitude": 127.301187652392
        }
        """;

        mockMvc.perform(post("/api/map/route")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())  // 200 OK 기대
                .andExpect(content().string("Mocked Kakao API response"));
    }
}
