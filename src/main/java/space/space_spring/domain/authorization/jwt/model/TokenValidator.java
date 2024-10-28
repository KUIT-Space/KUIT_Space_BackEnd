package space.space_spring.domain.authorization.jwt.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.entity.TokenStorage;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtUnauthorizedTokenException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.EXPIRED_REFRESH_TOKEN;
import static space.space_spring.response.status.BaseExceptionResponseStatus.TOKEN_MISMATCH;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final JwtLoginProvider jwtLoginProvider;

    public void validateRefreshToken(String refreshToken, TokenStorage tokenStorage) {
        // TODO 1. refresh token의 만료시간 체크
        if (jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)) {
            // refresh token이 만료된 경우 -> 예외 발생 -> 유저의 재 로그인 유도
            // db에서 row delete 하는 코드 추가
            throw new JwtExpiredTokenException(EXPIRED_REFRESH_TOKEN);
        }

        // TODO 2. refresh token이 db에 실제로 존재하는지 체크
        if (!tokenStorage.checkTokenValue(refreshToken)) {
            // refresh token이 db에 존재하지 않느 경우 -> 유효하지 않은 refresh token이므로 예외 발생
            // db에서 row delete 하는 코드 추가
            throw new JwtUnauthorizedTokenException(TOKEN_MISMATCH);
        }
    }

}
