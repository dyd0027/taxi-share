package taxi.share.back.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taxi.share.back.model.User;
import taxi.share.back.repository.UserRepository;
import taxi.share.back.util.JwtUtil;
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    public User registerUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }

    public User login(String userId, String password, HttpServletResponse response) throws Exception {
        User user = findUserByUserId(userId);
        if (passwordEncoder.matches(password, user.getUserPassword())) {
            jwtUtil.addTokenToCookie(jwtUtil.generateToken(user.getUserId()), response);
            return user;
        } else {
            throw new Exception("Invalid credentials");
        }
    }

    @Cacheable(value = "userCache", key = "#userId")
    public User findUserByUserId(String userId) throws Exception {
        // Redis에서 데이터 가져오는지 확인 하는 테스트 코드
        // 실제로 Redis cache에 저장되면 findUserByUserId메소드를 호출하지 않음
        // 작동 방식: 메서드가 호출될 때마다 캐시를 먼저 확인 -> 캐시가 있으면 캐시된 데이터를 반환 -> 캐시에 없으면 실제 메서드를 실행한 결과를 캐시에 저장하고 반환
//        Cache cache = cacheManager.getCache("userCache");
//        if (cache != null && cache.get(userId) != null) {
//            System.out.println("Cache hit for key: " + userId);
//        } else {
//            System.out.println("Cache miss for key: " + userId);
//        }
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("User not found"));
    }

    public boolean validateToken(String token) {
        String userId = jwtUtil.extractUserId(token);
        try {
            User user = findUserByUserId(userId);
            boolean isValidated = jwtUtil.validateToken(token, user);
            if(!isValidated) {
                redisTemplate.delete(userId);
            }
            return isValidated;
        } catch (Exception e){
            redisTemplate.delete(userId);
            log.error(e.getMessage());
            return false;
        }
    }
}