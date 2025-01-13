package taxi.share.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    @Autowired
    private MessageSender messageSender;

    public void notifyMatchingSuccess(String userAId, String userBId) {
        // 유저 A와 B에게 매칭 성공 메시지 전송
        String messageA = "매칭되었습니다. 상대: " + userBId;
        String messageB = "매칭되었습니다. 상대: " + userAId;

        messageSender.sendMessageToUser(userAId, messageA);
        messageSender.sendMessageToUser(userBId, messageB);
    }
}