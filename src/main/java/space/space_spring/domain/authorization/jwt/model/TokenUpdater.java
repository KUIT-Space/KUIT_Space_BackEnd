package space.space_spring.domain.authorization.jwt.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;

@Component
@RequiredArgsConstructor
public class TokenUpdater {

    private final JwtLoginProvider jwtLoginProvider;

    public TokenPairDTO updateTokenPair(User user, TokenStorage tokenStorage) {
        // new access token, new refresh token 발급 받아서
        String newAccessToken = jwtLoginProvider.generateToken(user, TokenType.ACCESS);
        String newRefreshToken = jwtLoginProvider.generateToken(user, TokenType.REFRESH);

        // tokenStorage update 하고
        tokenStorage.updateTokenValue(newRefreshToken);

        return TokenPairDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
