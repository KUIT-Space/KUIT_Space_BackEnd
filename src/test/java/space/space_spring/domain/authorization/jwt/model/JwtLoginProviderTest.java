package space.space_spring.domain.authorization.jwt.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtLoginProviderTest {

    @Test
    @DisplayName("userId를 인자로 넘기면, userId값을 payload에 가지고 있는 access token을 만들어 줍니다.")
    void generateToken() throws Exception {
        //given
        Long userId = 1L;
        JwtLoginProvider jwtLoginProvider = new JwtLoginProvider();

        jwtLoginProvider.ACCESS_SECRET_KEY = "Kv7yGnLFcgfx7F9MyrBuBp4hbhtsAiY17Mo7TlUpDQU";
        jwtLoginProvider.REFRESH_SECRET_KEY = "UvfDATy9MEEg3rWvxrQ0JBc5gC4MFyxDS6VXLCcjou4";
        jwtLoginProvider.ACCESS_EXPIRED_IN = 3600000L;          // 1시간
        jwtLoginProvider.REFRESH_EXPIRED_IN = 604800000L;       // 7일

        //when
        String accessToken = jwtLoginProvider.generateToken(userId, TokenType.ACCESS);

        //then
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(jwtLoginProvider.ACCESS_SECRET_KEY)
                .build()
                .parseClaimsJws(accessToken);

        Long extractedUserId = claims.getBody().get("userId", Long.class);

        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("")
    void test() throws Exception {
        //given

        //when

        //then
    }


}