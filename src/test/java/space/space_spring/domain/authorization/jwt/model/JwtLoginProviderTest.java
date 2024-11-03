package space.space_spring.domain.authorization.jwt.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(JwtLoginProvider.class)
@TestPropertySource(properties = {          // 테스트용 프로퍼티 소스(환경 설정) 지정
        "secret.jwt.access-secret-key=accessSecretKeyaccessSecretKeyaccessSecretKey",
        "secret.jwt.refresh-secret-key=refreshSecretKeyrefreshSecretKeyrefreshSecretKey",
        "secret.jwt.access-expired-in=3600000",
        "secret.jwt.refresh-expired-in=604800000"
})
class JwtLoginProviderTest {

    @Autowired
    private JwtLoginProvider jwtLoginProvider;

    @Test
    @DisplayName("userId를 인자로 넘기면, userId값을 payload에 가지고 있는 access token을 만들어 줍니다.")
    void generateToken() throws Exception {
        //given
        Long userId = 1L;

        //when
        String accessToken = jwtLoginProvider.generateToken(userId, TokenType.ACCESS);

        //then
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(jwtLoginProvider.getACCESS_SECRET_KEY())
                .build()
                .parseClaimsJws(accessToken);

        Long extractedUserId = claims.getBody().get("userId", Long.class);

        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("인자로 받은 access token의 유효기간이 끝나지 않았다면, false를 return 한다.")
    void isExpiredToken1() throws Exception {
        //given
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 360000L))       // 현 시점부터 1시간 동안이 유효기간
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        //when
        boolean expiredToken = jwtLoginProvider.isExpiredToken(accessToken, TokenType.ACCESS);

        //then
        assertThat(expiredToken).isFalse();
    }

    @Test
    @DisplayName("인자로 받은 access token의 유효기간이 끝났다면, true를 return 한다.")
    void isExpiredToken2() throws Exception {
        //given
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        //when
        boolean expiredToken = jwtLoginProvider.isExpiredToken(accessToken, TokenType.ACCESS);

        //then
        assertThat(expiredToken).isTrue();
    }

    @Test
    @DisplayName("인자로 받은 refresh token의 유효기간이 끝나지 않았다면, false를 return 한다.")
    void isExpiredToken3() throws Exception {
        //given
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 360000L))       // 현 시점부터 1시간 동안이 유효기간
                .signWith(SignatureAlgorithm.HS256, "refreshSecretKeyrefreshSecretKeyrefreshSecretKey")
                .compact();

        //when
        boolean expiredToken = jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH);

        //then
        assertThat(expiredToken).isFalse();
    }

    @Test
    @DisplayName("인자로 받은 refresh token의 유효기간이 끝났다면, true를 return 한다.")
    void isExpiredToken4() throws Exception {
        //given
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .signWith(SignatureAlgorithm.HS256, "refreshSecretKeyrefreshSecretKeyrefreshSecretKey")
                .compact();

        //when
        boolean expiredToken = jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH);

        //then
        assertThat(expiredToken).isTrue();
    }

    @Test
    @DisplayName("만료되지 않은 access token의 payload에 담긴 userId 값을 return 한다.")
    void getUserIdFromAccessToken1() throws Exception {
        //given
        Long userId = 1L;
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 360000L))       // 현 시점부터 1시간 동안이 유효기간
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        //when
        Long userIdFromAccessToken = jwtLoginProvider.getUserIdFromAccessToken(accessToken);

        //then
        assertThat(userIdFromAccessToken).isEqualTo(userId);
    }

    @Test
    @DisplayName("만료된 access token의 payload에 담긴 userId 값을 return 한다.")
    void getUserIdFromAccessToken2() throws Exception {
        //given
        Long userId = 1L;
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setExpiration(new Date(now.getTime()))       // 생성하자마자 유효기간 끝
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "accessSecretKeyaccessSecretKeyaccessSecretKey")
                .compact();

        //when
        Long userIdFromAccessToken = jwtLoginProvider.getUserIdFromAccessToken(accessToken);

        //then
        assertThat(userIdFromAccessToken).isEqualTo(userId);
    }
}