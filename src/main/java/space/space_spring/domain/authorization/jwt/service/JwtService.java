package space.space_spring.domain.authorization.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.*;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.domain.user.repository.UserRepository;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;
import space.space_spring.exception.CustomException;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtUnauthorizedTokenException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final JwtLoginProvider jwtLoginProvider;
    private final TokenResolver tokenResolver;
    private final TokenValidator tokenValidator;
    private final TokenUpdater tokenUpdater;

    @Transactional
    public TokenPairDTO updateAccessToken(HttpServletRequest request) {
        // request에서 기존의 TokenPair를 찾아와서
        TokenPairDTO oldTokenPair = tokenResolver.resolveTokenPair(request);

        // 여기서 User 찾고
        User user = getUserByAccessToken(oldTokenPair.getAccessToken());

        // 이 User로 refresh token의 유효성 검사 진행하고
        validateRefreshToken(user, oldTokenPair.getRefreshToken());

        // access, refresh 새로 발급
        return updateTokenPair(user);
    }

    private User getUserByAccessToken(String accessToken) {
        Long userIdFromToken = jwtLoginProvider.getUserIdFromAccessToken(accessToken);

        return userRepository.findByUserId(userIdFromToken)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private void validateRefreshToken(User user, String refreshToken) {
        TokenStorage tokenStorage = jwtRepository.findByUser(user)
                .orElseThrow(() ->
                {
                    // db에서 row delete 하는 코드 추가
                    jwtRepository.deleteByUser(user);
                    throw new JwtUnauthorizedTokenException(TOKEN_MISMATCH);
                });

        try {
            tokenValidator.validateRefreshToken(refreshToken, tokenStorage);
        } catch (JwtUnauthorizedTokenException e) {
            jwtRepository.deleteByUser(user);
            throw e;
        }
    }

    private TokenPairDTO updateTokenPair(User user) {
        TokenStorage tokenStorage = jwtRepository.findByUser(user)
                .orElseThrow(() -> new JwtUnauthorizedTokenException(TOKEN_MISMATCH));

        return tokenUpdater.updateTokenPair(user, tokenStorage);
    }

    public TokenPairDTO provideJwtToOAuthUser(User userByOAuthInfo) {
        String accessToken = jwtLoginProvider.generateToken(userByOAuthInfo.getUserId(), TokenType.ACCESS);
        String refreshToken = jwtLoginProvider.generateToken(userByOAuthInfo.getUserId(), TokenType.REFRESH);

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
