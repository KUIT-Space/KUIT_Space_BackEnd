package space.space_spring.domain.authorization.jwt.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import space.space_spring.exception.jwt.bad_request.JwtNoTokenException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.TOKEN_NOT_FOUND;
import static space.space_spring.response.status.BaseExceptionResponseStatus.UNSUPPORTED_TOKEN_TYPE;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtLoginTokenResolver {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    public TokenPairDTO resolveTokenPair(HttpServletRequest request) {
        // TODO 1. access token 파싱
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(accessToken);

        // TODO 2. refresh token 파싱
        String refreshToken = request.getHeader("Authorization-refresh");
        validateToken(refreshToken);

        // TODO 3. return
        return TokenPairDTO.builder()
                .accessToken(accessToken.substring(JWT_TOKEN_PREFIX.length()))
                .refreshToken(refreshToken.substring(JWT_TOKEN_PREFIX.length()))
                .build();
    }

    private void validateToken(String token) {
        if (token == null) {
            throw new JwtNoTokenException(TOKEN_NOT_FOUND);
        }
        if (!token.startsWith(JWT_TOKEN_PREFIX)) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        }
    }

}
