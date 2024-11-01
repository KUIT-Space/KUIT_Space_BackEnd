package space.space_spring.domain.authorization.jwt.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import space.space_spring.exception.jwt.bad_request.JwtNoTokenException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtLoginTokenResolverTest {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    @Test
    @DisplayName("request header로부터 access token, refresh token을 rosolve한다.")
    void resolveTokenPair() throws Exception {
        //given
        JwtLoginTokenResolver tokenResolver = new JwtLoginTokenResolver();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", JWT_TOKEN_PREFIX + "accessToken");
        request.addHeader("Authorization-refresh", JWT_TOKEN_PREFIX + "refreshToken");

        //when
        TokenPairDTO tokenPairDTO = tokenResolver.resolveTokenPair(request);

        //then
        assertThat(tokenPairDTO.getAccessToken()).isEqualTo("accessToken");
        assertThat(tokenPairDTO.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("access token값이 null 일 경우 예외를 던진다.")
    void validateToken1() throws Exception {
        //given
        JwtLoginTokenResolver tokenResolver = new JwtLoginTokenResolver();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization-refresh", JWT_TOKEN_PREFIX + "refreshToken");

        //when //then
        assertThatThrownBy(() -> tokenResolver.resolveTokenPair(request))
                .isInstanceOf(JwtNoTokenException.class)
                .hasMessage("토큰이 HTTP Header에 없습니다.");
    }

    @Test
    @DisplayName("refresh token값이 null 일 경우 예외를 던진다.")
    void validateToken2() throws Exception {
        //given
        JwtLoginTokenResolver tokenResolver = new JwtLoginTokenResolver();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", JWT_TOKEN_PREFIX + "accessToken");

        //when //then
        assertThatThrownBy(() -> tokenResolver.resolveTokenPair(request))
                .isInstanceOf(JwtNoTokenException.class)
                .hasMessage("토큰이 HTTP Header에 없습니다.");
    }

    @Test
    @DisplayName("토큰값의 prefix가 잘못된 경우 예외를 던진다.")
    void validateToken3() throws Exception {
        //given
        JwtLoginTokenResolver tokenResolver = new JwtLoginTokenResolver();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "wrong prefix " + "accessToken");
        request.addHeader("Authorization-refresh", "wrong prefix " + "refreshToken");

        //when //then
        assertThatThrownBy(() -> tokenResolver.resolveTokenPair(request))
                .isInstanceOf(JwtUnsupportedTokenException.class)
                .hasMessage("지원되지 않는 토큰 형식입니다.");
    }
}