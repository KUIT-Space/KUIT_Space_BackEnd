package space.space_spring.domain.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.EXPIRED_REFRESH_TOKEN;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_REFRESH_TOKEN;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TOKEN_MISMATCH;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.user.adapter.in.web.TokenPair;
import space.space_spring.domain.user.application.port.out.CreateRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.DeleteRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.LoadRefreshTokenPort;
import space.space_spring.global.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.global.exception.jwt.unauthorized.JwtInvalidTokenException;
import space.space_spring.global.exception.jwt.unauthorized.JwtUnauthorizedTokenException;

public class TokenServiceTest {

    private JwtLoginProvider jwtLoginProvider;
    private LoadSpaceMemberPort loadSpaceMemberPort;
    private LoadRefreshTokenPort loadRefreshTokenPort;
    private CreateRefreshTokenPort createRefreshTokenPort;
    private DeleteRefreshTokenPort deleteRefreshTokenPort;
    private TokenService tokenService;

    private static SpaceMember spaceMember;
    private static final String accessToken = "expired-access-token";
    private static final String refreshToken = "old-refresh-token";

    @BeforeEach
    void setup() throws JsonProcessingException {
        jwtLoginProvider = Mockito.mock(JwtLoginProvider.class);
        loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
        loadRefreshTokenPort = Mockito.mock(LoadRefreshTokenPort.class);
        createRefreshTokenPort = Mockito.mock(CreateRefreshTokenPort.class);
        deleteRefreshTokenPort = Mockito.mock(DeleteRefreshTokenPort.class);
        tokenService = new TokenService(jwtLoginProvider, loadSpaceMemberPort, loadRefreshTokenPort, createRefreshTokenPort, deleteRefreshTokenPort);

        spaceMember = SpaceMember.create(1L, 0L, 0L, 0L, "nickname", "profileImageUrl", false);

        Mockito.when(jwtLoginProvider.getSpaceMemberIdFromAccessToken(accessToken)).thenReturn(spaceMember.getId());
        Mockito.when(loadSpaceMemberPort.loadById(spaceMember.getId())).thenReturn(spaceMember);
        Mockito.when(loadRefreshTokenPort.loadByUserId(spaceMember.getUserId())).thenReturn(Optional.of(refreshToken));
    }

    @Test
    @DisplayName("access token이 만료된 경우, 유효한 refresh token을 통해 토큰 쌍을 재발급받는다.")
    void updateTokenPair() {
        //given
        Mockito.when(jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)).thenReturn(false);
        Mockito.when(jwtLoginProvider.generateAccessToken(spaceMember.getSpaceId(), spaceMember.getId())).thenReturn("new-access-token");
        Mockito.when(jwtLoginProvider.generateRefreshToken(spaceMember.getUserId())).thenReturn("new-refresh-token");

        //when
        TokenPair expiredTokenPair = new TokenPair(accessToken, refreshToken);
        TokenPair newTokenPair = tokenService.updateTokenPair(expiredTokenPair);

        //then
        assertThat(newTokenPair.getAccessToken()).isEqualTo("new-access-token");
        assertThat(newTokenPair.getRefreshToken()).isEqualTo("new-refresh-token");
    }

    @Test
    @DisplayName("refresh token이 DB에 존재하지 않는 경우 JwtExpiredTokenException을 발생시킨다.")
    void updateTokenPair_invalid_refresh_token() {
        //given
        Mockito.when(loadRefreshTokenPort.loadByUserId(spaceMember.getUserId())).thenReturn(Optional.empty());

        //when
        TokenPair expiredTokenPair = new TokenPair(accessToken, refreshToken);

        //then
        assertThatThrownBy(() -> tokenService.updateTokenPair(expiredTokenPair))
                .isInstanceOf(JwtInvalidTokenException.class)
                .hasMessage(INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    @DisplayName("refresh token이 만료된 경우 JwtExpiredTokenException을 발생시킨다.")
    void updateTokenPair_expired_refresh_token() {
        //given
        Mockito.when(jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)).thenReturn(true);

        //when
        TokenPair expiredTokenPair = new TokenPair(accessToken, refreshToken);

        //then
        assertThatThrownBy(() -> tokenService.updateTokenPair(expiredTokenPair))
                .isInstanceOf(JwtExpiredTokenException.class)
                .hasMessage(EXPIRED_REFRESH_TOKEN.getMessage());
    }

    @Test
    @DisplayName("refresh token이 일치하지 않는 경우 JwtExpiredTokenException을 발생시킨다.")
    void updateTokenPair_different_refresh_token() {
        //given
        Mockito.when(jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)).thenReturn(false);

        //when
        TokenPair expiredTokenPair = new TokenPair(accessToken, "old-refresh-token-2");

        //then
        assertThatThrownBy(() -> tokenService.updateTokenPair(expiredTokenPair))
                .isInstanceOf(JwtUnauthorizedTokenException.class)
                .hasMessage(TOKEN_MISMATCH.getMessage());
    }
}
