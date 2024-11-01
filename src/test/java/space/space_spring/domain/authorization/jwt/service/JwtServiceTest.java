package space.space_spring.domain.authorization.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.authorization.jwt.model.TokenPairDTO;
import space.space_spring.domain.authorization.jwt.model.JwtLoginTokenResolver;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.domain.user.repository.UserRepository;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtUnauthorizedTokenException;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({JwtService.class, JwtLoginProvider.class, JwtLoginTokenResolver.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "secret.jwt.access-secret-key=accessSecretKeyaccessSecretKeyaccessSecretKey",
        "secret.jwt.refresh-secret-key=refreshSecretKeyrefreshSecretKeyrefreshSecretKey",
        "secret.jwt.access-expired-in=3600000",
        "secret.jwt.refresh-expired-in=604800000"
})
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, JwtRepository.class})
@EntityScan(basePackageClasses = {User.class, TokenStorage.class})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtRepository jwtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtLoginProvider jwtLoginProvider;

    @Autowired
    private JwtLoginTokenResolver tokenResolver;

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    @Test
    @DisplayName("request의 refresh token이 유효한 토큰인 경우, 새로운 token pair를 발급하고 db의 token을 update 한다.")
    void updateTokenPair1() throws Exception {
        //given
        User user = User.create("email", "password", "name", UserSignupType.LOCAL);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 360000L))       // 현 시점부터 1시간 동안이 유효기간
                .signWith(SignatureAlgorithm.HS256, "refreshSecretKeyrefreshSecretKeyrefreshSecretKey")
                .compact();

        TokenStorage tokenStorage = TokenStorage.create(savedUser, refreshToken);
        jwtRepository.save(tokenStorage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", JWT_TOKEN_PREFIX + accessToken);
        request.addHeader("Authorization-refresh", JWT_TOKEN_PREFIX + refreshToken);

        //when
        TokenPairDTO tokenPairDTO = jwtService.updateTokenPair(request);

        //then
        String newAccessToken = tokenPairDTO.getAccessToken();
        String newRefreshToken = tokenPairDTO.getRefreshToken();

        Claims accessTokenClaims = Jwts.parser()
                .setSigningKey(jwtLoginProvider.getACCESS_SECRET_KEY())
                .parseClaimsJws(newAccessToken)
                .getBody();

        assertThat(accessTokenClaims.get("userId", Long.class)).isEqualTo(userId);

        TokenStorage updatedTokenStorage = jwtRepository.findByUser(savedUser)
                .orElseThrow(() -> new Exception("TokenStorage not found"));

        assertThat(updatedTokenStorage.getTokenValue()).isEqualTo(newRefreshToken);

        System.out.println("old access token = " + accessToken);
        System.out.println("new access token = " + newAccessToken);
        System.out.println("old refresh token = " + refreshToken);
        System.out.println("new refresh token = " + newRefreshToken);

    }

    @Test
    @DisplayName("request의 refresh token의 유효기간이 지났을 경우, 에러를 발생시키고 db의 token을 지운다.")
    void updateTokenPair2() throws Exception {
        //given
        User user = User.create("email", "password", "name", UserSignupType.LOCAL);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .signWith(SignatureAlgorithm.HS256, "refreshSecretKeyrefreshSecretKeyrefreshSecretKey")
                .compact();

        TokenStorage tokenStorage = TokenStorage.create(savedUser, refreshToken);
        jwtRepository.save(tokenStorage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", JWT_TOKEN_PREFIX + accessToken);
        request.addHeader("Authorization-refresh", JWT_TOKEN_PREFIX + refreshToken);

        //when //then
        assertThatThrownBy(() -> jwtService.updateTokenPair(request))
                .isInstanceOf(JwtExpiredTokenException.class)
                .hasMessage("만료된 refresh token 입니다. 다시 로그인해야합니다.");

        Optional<TokenStorage> byUser = jwtRepository.findByUser(user);
        assertThat(byUser).isEmpty();
    }

    @Test
    @DisplayName("request의 refresh token이 db에 저장된 token 값과 다를 경우, 에러를 발생시키고 db의 token을 지운다.")
    void updateTokenPair3() throws Exception {
        //given
        User user = User.create("email", "password", "name", UserSignupType.LOCAL);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 360000L))       // 현 시점부터 1시간 동안이 유효기간
                .signWith(SignatureAlgorithm.HS256, "refreshSecretKeyrefreshSecretKeyrefreshSecretKey")
                .compact();

        String anotherRefreshToken = "anotherRefreshToken";

        TokenStorage tokenStorage = TokenStorage.create(savedUser, anotherRefreshToken);
        jwtRepository.save(tokenStorage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", JWT_TOKEN_PREFIX + accessToken);
        request.addHeader("Authorization-refresh", JWT_TOKEN_PREFIX + refreshToken);

        //when //then
        assertThatThrownBy(() -> jwtService.updateTokenPair(request))
                .isInstanceOf(JwtUnauthorizedTokenException.class)
                .hasMessage("저장된 refresh token 과 전달받은 refresh token 이 일치하지 않습니다. 다시 로그인해야합니다.");

        Optional<TokenStorage> byUser = jwtRepository.findByUser(user);
        assertThat(byUser).isEmpty();

        System.out.println("refresh token = " + refreshToken);
        System.out.println("another refresh token = " + anotherRefreshToken);
    }


}