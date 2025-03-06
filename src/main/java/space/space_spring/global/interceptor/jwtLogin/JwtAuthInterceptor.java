package space.space_spring.global.interceptor.jwtLogin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.exception.jwt.badRequest.JwtNoTokenException;
import space.space_spring.global.exception.jwt.badRequest.JwtUnsupportedTokenException;
import space.space_spring.global.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor{

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtLoginProvider jwtLoginProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = resolveAccessToken(request);

        if (jwtLoginProvider.isExpiredToken(accessToken, TokenType.ACCESS)) {
            throw new JwtExpiredTokenException(EXPIRED_ACCESS_TOKEN);
        }

        Long spaceIdFromToken = jwtLoginProvider.getSpaceIdFromAccessToken(accessToken);
        Long spaceMemberIdFromToken = jwtLoginProvider.getSpaceMemberIdFromAccessToken(accessToken);

        Long spaceIdFromUrl = extractSpaceIdFromUrl(request.getRequestURI());

        if (!spaceIdFromToken.equals(spaceIdFromUrl)) {
            throw new CustomException(SPACE_ID_PATHVARIABLE_ERROR);
        }

        request.setAttribute("spaceId", spaceIdFromToken);
        request.setAttribute("spaceMemberId", spaceMemberIdFromToken);

        return true;
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(token);
        return token.substring(JWT_TOKEN_PREFIX.length());
    }

    private Long extractSpaceIdFromUrl(String requestUri) {
        String[] uriParts = requestUri.split("/");
        if (uriParts.length > 2) {
            try {
                return Long.parseLong(uriParts[2]);
            } catch (NumberFormatException e) {
                throw new CustomException(SPACE_ID_PATHVARIABLE_ERROR);
            }
        }
        return null;
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