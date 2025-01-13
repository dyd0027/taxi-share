package taxi.share.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import taxi.share.back.service.MessageSender;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/test")
public class WebSocketTestController {

    private MessageSender messageSender; // 메시지 전송 클래스
    @GetMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "cookie", required = false) String cookie) {
        String userId = "userA";
        String message = "매칭되었습니다!";
        messageSender.sendMessageToUser(userId, message);

        // 메시지가 전송되었는지 로그나 디버그로 확인
        System.out.println("Message sent to user: " + userId + " | Message: " + message);
    }
    @GetMapping("/test2")
    public void test2(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "cookie", required = false) String cookie) {
        // 특정 토픽으로 브로드캐스트 메시지 전송
        String topic = "general";
        String message = "전체 메시지입니다!";
        messageSender.sendBroadcastMessage(topic, message);

        // 메시지가 전송되었는지 로그나 디버그로 확인
        System.out.println("Broadcast message sent to topic: " + topic + " | Message: " + message);
    }
}
