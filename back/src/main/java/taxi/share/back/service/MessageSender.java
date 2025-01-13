package taxi.share.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 특정 사용자에게 메시지 전송
    public void sendMessageToUser(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/match", message);
    }

    // 모든 클라이언트에게 메시지 전송 (브로드캐스트)
    public void sendBroadcastMessage(String topic, String message) {
        messagingTemplate.convertAndSend("/topic/" + topic, message);
    }
}