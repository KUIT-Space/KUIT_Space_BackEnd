package space.space_spring.global.interceptor.jwtLogin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.global.exception.jwt.badRequest.JwtNoTokenException;
import space.space_spring.global.exception.jwt.badRequest.JwtUnsupportedTokenException;
import space.space_spring.global.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class JwtLoginAuthInterceptor implements HandlerInterceptor{

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtLoginProvider jwtLoginProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 1. request header에서 access token 파싱
        String accessToken = resolveAccessToken(request);

        // TODO 2. AT 유효성 검사
        if (jwtLoginProvider.isExpiredToken(accessToken, TokenType.ACCESS)) {
            throw new JwtExpiredTokenException(EXPIRED_ACCESS_TOKEN);
        }

        // TODO 3. AT 의 payload 로 부터 spaceMemberId 값 get
        Long spaceMemberIdFromToken = jwtLoginProvider.getSpaceMemberIdFromAccessToken(accessToken);
        request.setAttribute("spaceMemberId", spaceMemberIdFromToken);

        return true;
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(token);
        return token.substring(JWT_TOKEN_PREFIX.length());
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