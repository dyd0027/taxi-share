package taxi.share.back.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import taxi.share.back.model.User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "taxi_share";
    private static final long EXPIRATION_TIME = 1_000; // 10 minutes
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");

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
    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, User user) {
        final String userId = extractUserId(token);
        return (userId.equals(user.getUserId()) && !isTokenExpired(token));
    }
    public String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(Base64.encodeBase64String(SECRET_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
    public void invalidateCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
