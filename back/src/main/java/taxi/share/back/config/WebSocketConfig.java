package taxi.share.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트로 메시지를 보낼 브로커 경로 설정
        config.enableSimpleBroker("/queue", "/topic"); // 클라이언트가 구독할 경로
        config.setApplicationDestinationPrefixes("/app"); // 서버가 처리할 메시지 경로 (클라이언트에서 보낼 때 사용, 필요 없으면 무시)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 설정
        registry.addEndpoint("/ws") // 클라이언트가 WebSocket 연결 요청할 경로
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 (CORS 설정)
                .withSockJS(); // SockJS 지원
    }
}
