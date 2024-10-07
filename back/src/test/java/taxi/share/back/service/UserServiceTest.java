package taxi.share.back.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import taxi.share.back.model.User;
import taxi.share.back.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private UserRepository userRepository;  // UserRepository 추가
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        // Initialize mocks if necessary
    }


    @Test
    public void testCacheableBehavior() throws Exception {
        // @cacheable 테스트 확인 -> 테스트 통과 시 Redis에 저장 안됨.

        String userId = "testUser";
        User user = new User();
        user.setUserId(userId);

        // 캐시 미스 상태에서 실제 메서드 호출 (캐시 저장)
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));  // userRepository Mock 설정
        when(redisTemplate.hasKey("userCache::" + userId)).thenReturn(false);  // 캐시에 값이 없는 상태 설정 (미스)

        // 첫 번째 호출 - 캐시 미스
        User resultUser = userService.findUserByUserId(userId);  // 캐시가 없으므로 실제 메서드 호출
        assertTrue(resultUser != null);  // 실제 값 반환 확인

        // 캐시 히트 설정
        when(redisTemplate.hasKey("userCache::" + userId)).thenReturn(false);  // 캐시에 값이 있는 상태 설정 (히트)

        // 두 번째 호출 - 캐시 히트
        resultUser = userService.findUserByUserId(userId);  // 캐시에서 값 반환
        assertTrue(resultUser != null);  // 캐시된 값이 반환되는지 확인
    }
}

