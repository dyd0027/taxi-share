package taxi.share.back.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "taxi_share";
    private static final long EXPIRATION_TIME = 600_000; // 10 minutes
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");

    public String generateToken(String userId) {
        ZonedDateTime now = ZonedDateTime.now(KST_ZONE_ID);
        ZonedDateTime expirationTime = now.plusMinutes(10);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expirationTime.toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public void addTokenToCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (EXPIRATION_TIME)); // Convert milliseconds to seconds
        response.addCookie(cookie);
    }
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.encodeBase64String(SECRET_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(Base64.encodeBase64String(SECRET_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
