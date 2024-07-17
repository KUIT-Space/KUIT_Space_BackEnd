package space.space_spring.interceptor.jwtUserSpace;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import space.space_spring.dto.jwt.JwtPayloadDto;
import space.space_spring.jwt.JwtUserSpaceProvider;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.bad_request.JwtNoTokenException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class JwtUserSpaceAuthInterceptor implements HandlerInterceptor {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtUserSpaceProvider jwtUserSpaceProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = resolveAccessToken(request);
        validateAccessToken(accessToken);

        // jwt에서 JwtPayloadDto get
        JwtPayloadDto jwtPayloadDto = jwtUserSpaceProvider.getJwtPayloadDtoFromToken(accessToken);
        request.setAttribute("jwtPayloadDto", jwtPayloadDto);

        return true;
    }

    private void validateAccessToken(String accessToken) {
        if (jwtUserSpaceProvider.isExpiredToken(accessToken)) {
            throw new JwtExpiredTokenException(EXPIRED_TOKEN);
        }

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
