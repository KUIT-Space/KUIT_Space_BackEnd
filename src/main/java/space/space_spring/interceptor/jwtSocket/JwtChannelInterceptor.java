package space.space_spring.interceptor.jwtSocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import space.space_spring.dto.jwt.TokenType;
import space.space_spring.exception.jwt.bad_request.JwtNoTokenException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.jwt.JwtLoginProvider;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtLoginProvider jwtLoginProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            String validatedToken = validateAccessToken(jwtToken);

            // 검증 후 사용자 정보를 세션에 저장
            Long userId = jwtLoginProvider.getUserIdFromToken(validatedToken, TokenType.ACCESS);
            accessor.getSessionAttributes().put("userId", userId);
        }
        return message;
    }

    private String validateAccessToken(String token) {
        // token 존재 유무 validate
        if (token == null) {
            throw new JwtNoTokenException(TOKEN_NOT_FOUND);
        }
        if (!token.startsWith(JWT_TOKEN_PREFIX)) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        }

        // prefix 제거
        String tokenWithoutPrefix = token.substring(JWT_TOKEN_PREFIX.length());

        // access token 값 validate
        if (jwtLoginProvider.isExpiredToken(tokenWithoutPrefix, TokenType.ACCESS)) {
            throw new JwtExpiredTokenException(EXPIRED_ACCESS_TOKEN);
        }

        return tokenWithoutPrefix;
    }
}
