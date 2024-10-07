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
        jwtLoginProvider.validateRefreshToken(userByAccessToken, oldTokenPair.getRefreshToken());

        // access, refresh 새로 발급
        TokenPairDTO tokenPairDTO = updateTokenPair(userByAccessToken);

        return tokenPairDTO;
    }

    private User getUserByAccessToken(String accessToken) {
        Long userIdFromToken = jwtLoginProvider.getUserIdFromAccessToken(accessToken);

        return userRepository.findByUserId(userIdFromToken)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private TokenPairDTO updateTokenPair(User user) {
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
