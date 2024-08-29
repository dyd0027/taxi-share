package taxi.share.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import taxi.share.back.model.Routes;

@Slf4j
@Service
public class MapService {
    @Value("${kakao.rest.api.key}")
    private String kakaoApiKey;

    public void route(Routes routes){
        log.info("출발지 >>> {}",routes.getOrigin());
        log.info("kakaoApiKey >>> {}",kakaoApiKey);

    }
}
