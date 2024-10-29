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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.authorization.jwt.model.TokenPairDTO;
import space.space_spring.domain.authorization.jwt.model.TokenResolver;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.domain.user.repository.UserRepository;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JwtService.class, JwtLoginProvider.class, TokenResolver.class})
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
    private TokenResolver tokenResolver;

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    @Test
    @DisplayName("request의 refresh token이 유효한 토큰인 경우, 새로운 token pair를 발급하고 db의 token을 update 한다.")
    void updateTokenPair() throws Exception {
        //given
        User user = User.create("email", "password", "name", UserSignupType.LOCAL);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 현 시점부터 1시간 동안이 유효기간
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 360000L))       // 생성하자마자 유효기간 끝
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

        // 새로운 액세스 토큰 검증
        Claims accessTokenClaims = Jwts.parser()
                .setSigningKey(jwtLoginProvider.getACCESS_SECRET_KEY())
                .parseClaimsJws(newAccessToken)
                .getBody();

        assertThat(accessTokenClaims.get("userId", Long.class)).isEqualTo(userId);

        // 토큰 저장소에 저장된 리프레시 토큰이 업데이트되었는지 확인
        TokenStorage updatedTokenStorage = jwtRepository.findByUser(savedUser)
                .orElseThrow(() -> new Exception("TokenStorage not found"));

        assertThat(updatedTokenStorage.getTokenValue()).isEqualTo(newRefreshToken);
    }


}