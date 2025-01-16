package taxi.share.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {


    private final MessageSender messageSender;
    private final UserService userService;
    public void notifyMatchingSuccess(int userANo, int userBNo) {
        // 유저 A와 B에게 매칭 성공 메시지 전송
        try {
            String message = "매칭되었습니다.";
            userService.findUserByUserNo(userANo);
            messageSender.sendMessageToUser(userService.findUserByUserNo(userANo).getUserId(), message);
            messageSender.sendMessageToUser(userService.findUserByUserNo(userBNo).getUserId(), message);
        }catch (Exception e){
            log.error("findUserByUserNo error");
        }
    }
}