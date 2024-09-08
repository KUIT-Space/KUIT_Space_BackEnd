package space.space_spring.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.space_spring.dto.jwt.TokenType;
import space.space_spring.entity.User;
import space.space_spring.exception.CustomException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtInvalidTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtMalformedTokenException;

import java.util.Date;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
public class JwtLoginProvider {
    @Value("${secret.jwt-login-secret-key}")
    private String JWT_LOGIN_SECRET_KEY;

    @Value("${secret.jwt-expired-in}")
    private Long JWT_EXPIRED_IN;


    public String generateToken(User user, TokenType tokenType) {
//        Claims claims = Jwts.claims().setSubject(jwtPayloadDto.getUserId().toString());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRED_IN);

        Long userId = user.getUserId();

        return Jwts.builder()
//                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, JWT_LOGIN_SECRET_KEY)
                .compact();
    }

    public boolean isExpiredToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_LOGIN_SECRET_KEY).build()
                    .parseClaimsJws(accessToken);
            return claims.getBody().getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;

        }catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (SignatureException e){
            throw new CustomException(WRONG_SIGNATURE_JWT);
        }catch (JwtException e) {
            log.error("[JwtTokenProvider.validateAccessToken]", e);
            throw e;
        }

    }

    public Long getUserIdFromToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_LOGIN_SECRET_KEY).build()
                    .parseClaimsJws(accessToken);
            return claims.getBody().get("userId", Long.class);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.getJwtPayloadDtoFromToken]", e);
            throw e;
        }
    }
}
