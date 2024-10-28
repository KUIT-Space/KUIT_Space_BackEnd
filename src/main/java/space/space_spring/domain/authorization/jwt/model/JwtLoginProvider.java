package space.space_spring.domain.authorization.jwt.model;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;
import space.space_spring.exception.CustomException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtInvalidTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtMalformedTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtUnauthorizedTokenException;

import java.util.Date;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLoginProvider {
    @Value("${secret.jwt.access-secret-key}")
    private String ACCESS_SECRET_KEY;

    @Value("${secret.jwt.refresh-secret-key}")
    private String REFRESH_SECRET_KEY;

    @Value("${secret.jwt.access-expired-in}")
    private Long ACCESS_EXPIRED_IN;

    @Value("${secret.jwt.refresh-expired-in}")
    private Long REFRESH_EXPIRED_IN;

    private final JwtRepository jwtRepository;

    public String generateToken(User user, TokenType tokenType) {
//        Claims claims = Jwts.claims().setSubject(jwtPayloadDto.getUserId().toString());

        Date now = new Date();
        Date expiration = setExpiration(now, tokenType);

        Long userId = user.getUserId();

        return makeToken(tokenType, userId, now, expiration);
    }

    private String makeToken(TokenType tokenType, Long userId, Date now, Date expiration) {
        if (tokenType.equals(TokenType.ACCESS)) {
            return Jwts.builder()
//                .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .claim("userId", userId)
                    .signWith(SignatureAlgorithm.HS256, choiceSecretKey(tokenType))
                    .compact();
        }

        return Jwts.builder()
//                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, choiceSecretKey(tokenType))
                .compact();
    }

    private String choiceSecretKey(TokenType tokenType) {
        if (tokenType.equals(TokenType.ACCESS)) {
            return ACCESS_SECRET_KEY;
        }
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

    public Long getUserIdFromAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_SECRET_KEY).build()
                    .parseClaimsJws(token);
            return claims.getBody().get("userId", Long.class);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서 userId 추출
            return e.getClaims().get("userId", Long.class);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.getJwtPayloadDtoFromToken]", e);
            throw e;
        }
    }

}
