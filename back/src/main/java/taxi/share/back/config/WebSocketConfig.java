package taxi.share.back.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import taxi.share.back.util.JwtUtil;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트로 메시지를 보낼 브로커 경로 설정
        config.enableSimpleBroker("/user", "/topic"); // 클라이언트가 구독할 경로
        config.setApplicationDestinationPrefixes("/app"); // 서버가 처리할 메시지 경로 (클라이언트에서 보낼 때 사용, 필요 없으면 무시)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 설정
        registry.addEndpoint("/ws") // 클라이언트가 WebSocket 연결 요청할 경로
                .addInterceptors(new HttpSessionHandshakeInterceptor(), new CustomHandshakeInterceptor()) // HTTP 헤더 전달
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 (CORS 설정)
                .withSockJS(); // SockJS 지원
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                // JWT를 쿠키에서 추출
                String jwt = (String) accessor.getSessionAttributes().get("jwt-token");
                // JWT를 검증하고 사용자 정보를 추출
                if (jwt != null) {
                    String userId = jwtUtil.getUserIdByToken(jwt);
                    accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null, null)); // Principal 설정
                }
                System.out.println("User Info: " + accessor.getUser()); // User가 제대로 설정되었는지 확인
                return message;
            }
        });
    }

}
