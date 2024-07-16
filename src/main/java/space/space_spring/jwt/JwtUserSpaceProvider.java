package space.space_spring.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.space_spring.dto.jwt.JwtPayloadDto;
import space.space_spring.dto.jwt.JwtUserSpaceAuthDto;
import space.space_spring.exception.jwt.unauthorized.JwtInvalidTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtMalformedTokenException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;

import java.util.Date;
import java.util.List;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Component
@Slf4j
public class JwtUserSpaceProvider {

    @Value("${secret.jwt-user-space-secret-key}")
    private String JWT_USER_SPACE_SECRET_KEY;

    @Value("${secret.jwt-expired-in}")
    private Long JWT_EXPIRED_IN;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(JwtPayloadDto jwtPayloadDto) {
//        Claims claims = Jwts.claims().setSubject(jwtPayloadDto.getUserId().toString());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRED_IN);

        Long userId = jwtPayloadDto.getUserId();
        List<JwtUserSpaceAuthDto> userSpaceList = jwtPayloadDto.getUserSpaceList();
        String userSpaceListToJson = null;
        try {
            userSpaceListToJson = objectMapper.writeValueAsString(userSpaceList);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert userSpaceList to json", e);
        }

        return Jwts.builder()
//                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("userId", userId)
                .claim("userSpaceList", userSpaceListToJson)
                .signWith(SignatureAlgorithm.HS256, JWT_USER_SPACE_SECRET_KEY)
                .compact();

    }

    public boolean isExpiredToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_USER_SPACE_SECRET_KEY).build()
                    .parseClaimsJws(accessToken);
            return claims.getBody().getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;

        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.validateAccessToken]", e);
            throw e;
        }
    }

    public JwtPayloadDto getJwtPayloadDtoFromToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_USER_SPACE_SECRET_KEY).build()
                    .parseClaimsJws(accessToken);

            Long userId = claims.getBody().get("userId", Long.class);
            String userSpaceListToJson = claims.getBody().get("userSpaceList", String.class);
            List<JwtUserSpaceAuthDto> userSpaceList = null;
            try {
                userSpaceList = objectMapper.readValue(userSpaceListToJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, JwtUserSpaceAuthDto.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to convert json to userSpaceList", e);
            }

            JwtPayloadDto jwtPayloadDto = new JwtPayloadDto();
            jwtPayloadDto.saveUserIdToJwt(userId);
            jwtPayloadDto.saveUserSpaceList(userSpaceList);

            return jwtPayloadDto;
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.getJwtPayloadDtoFromToken]", e);
            throw e;
        }
    }
}
