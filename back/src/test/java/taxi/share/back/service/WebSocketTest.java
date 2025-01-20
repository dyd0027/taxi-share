package taxi.share.back.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebSocketTest {

    @Autowired
    private MessageSender messageSender; // 메시지 전송 클래스

    @Test
    public void testSendMessageToUser() {
        // 특정 사용자에게 메시지 전송
        String userId = "userA";
        String message = "매칭되었습니다!";
        messageSender.sendMessageToUser(userId, message);

        // 메시지가 전송되었는지 로그나 디버그로 확인
        System.out.println("Message sent to user: " + userId + " | Message: " + message);
    }

    @Test
    public void testBroadcastMessage() {
        // 특정 토픽으로 브로드캐스트 메시지 전송
        String topic = "general";
        String message = "전체 메시지입니다!";
        messageSender.sendBroadcastMessage(topic, message);

        // 메시지가 전송되었는지 로그나 디버그로 확인
        System.out.println("Broadcast message sent to topic: " + topic + " | Message: " + message);
    }
}