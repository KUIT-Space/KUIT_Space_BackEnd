package space.space_spring.domain.user.adapter.in.web;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TOKEN_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNSUPPORTED_TOKEN_TYPE;

import jakarta.servlet.http.HttpServletRequest;
import space.space_spring.global.exception.jwt.badRequest.JwtNoTokenException;
import space.space_spring.global.exception.jwt.badRequest.JwtUnsupportedTokenException;

public class TokenParser {

    private final static String ACCESS_TOKEN_HEADER = "Authorization";
    private final static String REFRESH_TOKEN_HEADER = "Authorization-refresh";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static TokenPair parseTokenPair(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER).substring(TOKEN_PREFIX.length());
        validateToken(accessToken);

        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER).substring(TOKEN_PREFIX.length());
        validateToken(refreshToken);

        return new TokenPair(accessToken, refreshToken);
    }

    private static void validateToken(String token) {
        if (token == null) {
            throw new JwtNoTokenException(TOKEN_NOT_FOUND);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        }
    }
}
