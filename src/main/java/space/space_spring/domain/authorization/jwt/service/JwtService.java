package space.space_spring.domain.authorization.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.TokenResolver;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.domain.user.repository.UserRepository;
import space.space_spring.domain.authorization.jwt.model.TokenPairDTO;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;
import space.space_spring.exception.CustomException;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtUnauthorizedTokenException;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {

    private final JwtRepository jwtRepository;
    private final JwtLoginProvider jwtLoginProvider;
    private final UserRepository userRepository;
    private final TokenResolver tokenResolver;

    @Transactional
    public TokenPairDTO updateAccessToken(HttpServletRequest request) {
        // request에서 기존의 TokenPair를 찾아와서
        TokenPairDTO oldTokenPair = tokenResolver.resolveTokenPair(request);

        // 여기서 User 찾고
        User userByAccessToken = getUserByAccessToken(oldTokenPair.getAccessToken());

        // 이 User로 refresh token의 유효성 검사 진행하고
        validateRefreshToken(userByAccessToken, oldTokenPair.getRefreshToken());

        // access, refresh 새로 발급
        TokenPairDTO tokenPairDTO = updateTokenPair(userByAccessToken);

        return tokenPairDTO;
    }

    private User getUserByAccessToken(String accessToken) {
        Long userIdFromToken = jwtLoginProvider.getUserIdFromAccessToken(accessToken);

        return userRepository.findByUserId(userIdFromToken)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Transactional
    protected void validateRefreshToken(User user, String refreshToken) {
        TokenStorage tokenStorage = jwtRepository.findByUser(user)
                .orElseThrow(() ->
                {
                    // db에서 row delete 하는 코드 추가
                    jwtRepository.deleteByUser(user);
                    throw new JwtUnauthorizedTokenException(TOKEN_MISMATCH);
                });

        // TODO 1. refresh token의 만료시간 체크
        if (jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)) {
            // refresh token이 만료된 경우 -> 예외 발생 -> 유저의 재 로그인 유도
            // db에서 row delete 하는 코드 추가
            jwtRepository.deleteByUser(user);
            throw new JwtExpiredTokenException(EXPIRED_REFRESH_TOKEN);
        }

        // TODO 2. refresh token이 db에 실제로 존재하는지 체크
        if (!tokenStorage.checkTokenValue(refreshToken)) {
            // refresh token이 db에 존재하지 않느 경우 -> 유효하지 않은 refresh token이므로 예외 발생
            // db에서 row delete 하는 코드 추가
            jwtRepository.deleteByUser(user);
            throw new JwtUnauthorizedTokenException(TOKEN_MISMATCH);
        }
    }

    @Transactional
    protected TokenPairDTO updateTokenPair(User user) {
        // TODO 1. new access token, refresh token 발급
        String newAccessToken = jwtLoginProvider.generateToken(user, TokenType.ACCESS);
        String newRefreshToken = jwtLoginProvider.generateToken(user, TokenType.REFRESH);

        // TODO 2. db의 refresh token update
        TokenStorage tokenStorage = jwtRepository.findByUser(user)
                .orElseThrow(() -> new JwtUnauthorizedTokenException(TOKEN_MISMATCH));

        tokenStorage.updateTokenValue(newRefreshToken);

        // TODO 3. return
        return TokenPairDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public TokenPairDTO provideJwtToOAuthUser(User userByOAuthInfo) {
        String accessToken = jwtLoginProvider.generateToken(userByOAuthInfo, TokenType.ACCESS);
        String refreshToken = jwtLoginProvider.generateToken(userByOAuthInfo, TokenType.REFRESH);

        return TokenPairDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void updateRefreshToken(User user, String refreshToken) {
        TokenStorage tokenStorage = jwtRepository.findByUser(user)
                .orElseThrow(() -> new JwtUnauthorizedTokenException(TOKEN_MISMATCH));

        tokenStorage.updateTokenValue(refreshToken);
    }


}
