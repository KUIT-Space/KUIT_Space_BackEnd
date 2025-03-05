package space.space_spring.domain.user.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.user.adapter.in.web.TokenPair;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.domain.user.application.port.out.CreateRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.DiscordOauthPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiscordOauthService implements OauthUseCase {

    private final DiscordOauthPort discordOauthPort;
    private final LoadUserPort loadUserPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;
    private final JwtLoginProvider jwtLoginProvider;

    @Override
    public TokenPair signIn(String code) throws JsonProcessingException {
        String accessToken = discordOauthPort.getAccessToken(code);
        User user = discordOauthPort.getUserInfo(accessToken);
        return generateTokenPair(user);
    }

    private TokenPair generateTokenPair(User user) {
        Optional<User> savedUser = loadUserPort.loadUserByDiscordId(user.getDiscordId());

        if (savedUser.isPresent()) {
            Long userId = savedUser.get().getId();
            SpaceMember spaceMember = getSpaceMember(savedUser.get());
            String accessToken = jwtLoginProvider.generateAccessToken(spaceMember.getSpaceId(), spaceMember.getId());
            String refreshToken = jwtLoginProvider.generateRefreshToken(userId);
            createRefreshTokenPort.create(savedUser.get().getId(), refreshToken);
            return new TokenPair(accessToken, refreshToken);
        }
        return null;

    }

    private SpaceMember getSpaceMember(User savedUser) {
        return loadSpaceMemberPort.loadByUserId(savedUser.getId());
    }

}
