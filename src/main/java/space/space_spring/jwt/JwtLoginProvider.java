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
    @Value("${secret.access-secret-key}")
    private String ACCESS_SECRET_KEY;

    @Value("${secret.refresh-secret-key}")
    private String REFRESH_SECRET_KEY;

    @Value("${secret.access-expired-in}")
    private Long ACCESS_EXPIRED_IN;

    @Value("${secret.refresh-expired-in}")
    private Long REFRESH_EXPIRED_IN;

    public String generateToken(User user, TokenType tokenType) {
//        Claims claims = Jwts.claims().setSubject(jwtPayloadDto.getUserId().toString());

        Date now = new Date();
        Date expiration = setExpiration(now, tokenType);

        Long userId = user.getUserId();

        return Jwts.builder()
//                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, choiceSecretKey(tokenType))
                .compact();
    }

    private String choiceSecretKey(TokenType tokenType) {
        if (tokenType.equals(TokenType.ACCESS)) {
            System.out.println("access token : " + ACCESS_SECRET_KEY);
            return ACCESS_SECRET_KEY;
        }
        System.out.println("refresh token : " + REFRESH_SECRET_KEY);
        return REFRESH_SECRET_KEY;
    }

    private Date setExpiration(Date now, TokenType tokenType) {
        if (tokenType.equals(TokenType.ACCESS)) {
            // 엑세스 토큰 : 1시간
            return new Date(now.getTime() + ACCESS_EXPIRED_IN);
        }

        // 리프레쉬 토큰 : 7일
        return new Date(now.getTime() + REFRESH_EXPIRED_IN);
    }

    public boolean isExpiredToken(String token, TokenType tokenType) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(choiceSecretKey(tokenType)).build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;

        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (SignatureException e){
            throw new CustomException(WRONG_SIGNATURE_JWT);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.validateAccessToken]", e);
            throw e;
        }

    }

    public Long getUserIdFromToken(String token, TokenType tokenType) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(choiceSecretKey(tokenType)).build()
                    .parseClaimsJws(token);
            return claims.getBody().get("userId", Long.class);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.getJwtPayloadDtoFromToken]", e);
            throw e;
        }
    }
}
