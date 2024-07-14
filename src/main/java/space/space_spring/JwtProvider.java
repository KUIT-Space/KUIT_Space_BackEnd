package space.space_spring;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.space_spring.domain.User;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${secret.jwt-secret-key}")
    private String JWT_TOKEN_KEY;

    @Value("${secret.jwt-expired-in}")
    private Long JWT_EXPIRATION_TIME;

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("userId", user.getUserId())
                .signWith(SignatureAlgorithm.HS256, JWT_TOKEN_KEY)
                .compact();
    }
}
