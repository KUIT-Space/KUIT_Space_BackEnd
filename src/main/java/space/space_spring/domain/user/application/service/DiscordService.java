package space.space_spring.domain.user.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.domain.user.adapter.in.web.TokenPair;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.domain.user.application.port.out.DiscordOauthPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscordService implements OauthUseCase {

    private final DiscordOauthPort discordOauthPort;
    private final LoadUserPort loadUserPort;
    private final JwtLoginProvider jwtLoginProvider;

    @Override
    public TokenPair signInWithDiscord(String code) throws JsonProcessingException {
        String accessToken = discordOauthPort.getAccessToken(code);
        User user = discordOauthPort.getUserInfo(accessToken);
        return signIn(user);
    }

    private TokenPair signIn(User user) {
        Optional<User> savedUser = loadUserPort.loadUserByDiscordId(user.getDiscordId());

        if (savedUser.isPresent()) {
            String accessToken = jwtLoginProvider.generateToken(savedUser.get().getId(), TokenType.ACCESS);
            String refreshToken = jwtLoginProvider.generateToken(savedUser.get().getId(), TokenType.REFRESH);
            return new TokenPair(accessToken, refreshToken);
        }
        return null;
    }

}
