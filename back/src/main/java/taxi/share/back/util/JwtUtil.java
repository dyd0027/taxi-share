package taxi.share.back.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import taxi.share.back.model.User;
import taxi.share.back.repository.UserRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
@Slf4j
@Component
public class JwtUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SECRET_KEY = "taxi_share";
    private static final long EXPIRATION_TIME = 1_000; // 10 minutes
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
    @Autowired
    public JwtUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public String generateToken(String userId) {
        ZonedDateTime now = ZonedDateTime.now(KST_ZONE_ID);
        ZonedDateTime expirationTime = now.plusMinutes(1);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expirationTime.toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }
    public void addTokenToCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (EXPIRATION_TIME)); // Convert milliseconds to seconds
        // Cross-site 요청에서도 쿠키 전송
//        cookie.setDomain("localhost");
        response.addCookie(cookie);
    }
    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            // JWT를 파싱하여 내용(Claims)을 추출
            Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            // parseClaimsJws(token)에서 token값이 유효하면 true반환 아니라면 밑에 catch문을 타고 false반환
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우에도 Claims를 가져옴
            String userId = e.getClaims().getSubject();
            log.info("Http Cookie Session 만료 >>>> {}", userId);
            redisTemplate.delete("userCache::" + userId);

            // 쿠키값 초기화
            invalidateCookie(response, "jwt-token");
            return false;
        }
    }

    public String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public void invalidateCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
