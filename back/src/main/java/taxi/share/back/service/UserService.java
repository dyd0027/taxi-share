package taxi.share.back.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;
    private final RedisService redisService;

    public User registerUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }

    public User login(User dbUser, String password, HttpServletResponse response) throws Exception {
        if (passwordEncoder.matches(password, dbUser.getUserPassword())) {
            String userId = dbUser.getUserId();
            String cachedToken = cacheManager.getCache("tokenCache").get(userId, String.class);

            if (cachedToken != null) {
                jwtUtil.addTokenToCookie(cachedToken, response);
            } else {
                String token = jwtUtil.generateToken(userId);
                jwtUtil.addTokenToCookie(token, response);
                cacheManager.getCache("tokenCache").put(userId, token);
            }
            return dbUser;
        } else {
            log.error("Invalid password.");
            throw new Exception("Invalid credentials");
        }
    }

    // 박현빈 로컬에서 오류나서 밑에 있는 어노테이션 주석처리
    @Cacheable(value = "userCache", key = "#p0")
    public User findUserByUserId(String userId) throws Exception {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("User not found"));
    }

    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            return jwtUtil.validateToken(token, response);
        } catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}