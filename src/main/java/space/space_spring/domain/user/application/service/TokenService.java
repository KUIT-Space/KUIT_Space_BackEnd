package space.space_spring.domain.user.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.user.adapter.in.web.TokenPair;
import space.space_spring.domain.user.application.port.in.TokenUseCase;
import space.space_spring.domain.user.application.port.out.CreateRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.DeleteRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.LoadRefreshTokenPort;
import space.space_spring.global.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.global.exception.jwt.unauthorized.JwtInvalidTokenException;
import space.space_spring.global.exception.jwt.unauthorized.JwtUnauthorizedTokenException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService implements TokenUseCase {

    private final JwtLoginProvider jwtLoginProvider;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadRefreshTokenPort loadRefreshTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;

    @Override
    @Transactional
    public TokenPair updateTokenPair(TokenPair expiredTokenPair) {
        SpaceMember spaceMember = extractSpaceMember(expiredTokenPair.getAccessToken());

        String refreshToken = expiredTokenPair.getRefreshToken();
        String savedRefreshToken = loadRefreshTokenPort.loadByUserId(spaceMember.getUserId()).orElseThrow(() ->
                new JwtInvalidTokenException(INVALID_REFRESH_TOKEN)); // 해당 유저에 대한 refresh token이 저장돼있지 않은데 요청이 온 경우

        validateRefreshToken(spaceMember.getUserId(), refreshToken, savedRefreshToken);

        return updateTokenPair(spaceMember);
    }

    private SpaceMember extractSpaceMember(String accessToken) {
        Long spaceMemberId = jwtLoginProvider.getSpaceMemberIdFromAccessToken(accessToken);
        return loadSpaceMemberPort.loadById(spaceMemberId);
    }

    private void validateRefreshToken(Long userId, String refreshToken, String savedRefreshToken) {
        // TODO 1. refresh token의 만료시간 체크
        if (jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)) {
            deleteRefreshTokenStorage(userId);
            throw new JwtExpiredTokenException(EXPIRED_REFRESH_TOKEN);
        }

        // TODO 2. refresh token이 db에 존재하는 token 값과 일치하는지 확인
        if (!savedRefreshToken.equals(refreshToken)) {
            deleteRefreshTokenStorage(userId);
            throw new JwtUnauthorizedTokenException(TOKEN_MISMATCH);
        }
    }

    private TokenPair updateTokenPair(SpaceMember spaceMember) {
        String newAccessToken = jwtLoginProvider.generateToken(spaceMember.getUserId(), spaceMember.getId(), TokenType.ACCESS);
        String newRefreshToken = jwtLoginProvider.generateToken(spaceMember.getUserId(), spaceMember.getId(), TokenType.REFRESH);

        createRefreshTokenPort.create(spaceMember.getUserId(), newRefreshToken);

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void deleteRefreshTokenStorage(Long userId) {
        deleteRefreshTokenPort.deleteByUserId(userId);
    }

}
