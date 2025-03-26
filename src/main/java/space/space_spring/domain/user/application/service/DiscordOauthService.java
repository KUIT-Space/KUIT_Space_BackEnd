package space.space_spring.domain.user.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.user.adapter.in.web.SpaceInfo;
import space.space_spring.domain.user.adapter.in.web.TokenPair;
import space.space_spring.domain.user.adapter.in.web.SignInResult;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.domain.user.application.port.out.CreateRefreshTokenPort;
import space.space_spring.domain.user.application.port.out.DiscordOauthPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;
import space.space_spring.global.exception.CustomException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiscordOauthService implements OauthUseCase {

    @Value("${custom.defaultSpace}")
    private String DEFAULT_SPACE_NAME;

    private final DiscordOauthPort discordOauthPort;
    private final LoadUserPort loadUserPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadSpacePort loadSpacePort;
    private final CreateRefreshTokenPort createRefreshTokenPort;
    private final JwtLoginProvider jwtLoginProvider;

    @Override
    public SignInResult signIn(String code) throws JsonProcessingException {
        String accessToken = discordOauthPort.getAccessToken(code);
        User userFromDiscord = discordOauthPort.getUserInfo(accessToken);

        Optional<User> savedUser = loadUserPort.loadUserByDiscordId(userFromDiscord.getDiscordId());
        if (savedUser.isEmpty()) return SignInResult.createFail();

        Optional<SpaceMember> defaultSpaceMember = getDefaultSpaceMember(savedUser.get());
        if (defaultSpaceMember.isEmpty()) return SignInResult.createFail();

        TokenPair tokenPair = generateTokenPair(savedUser.get(), defaultSpaceMember.get());

        List<SpaceMember> spaceMembers = loadSpaceMemberPort.loadByUserId(savedUser.get().getId());
        List<SpaceInfo> spaceInfos = getSpaceInfos(spaceMembers);

        return SignInResult.createSuccess(tokenPair, spaceInfos);
    }

    private Optional<SpaceMember> getDefaultSpaceMember(User user) {
        return loadSpaceMemberPort.loadDefaultSpaceMember(user.getId(), DEFAULT_SPACE_NAME);
    }

    private TokenPair generateTokenPair(User user, SpaceMember spaceMember) {
        String accessToken = jwtLoginProvider.generateAccessToken(spaceMember.getSpaceId(), spaceMember.getId());
        String refreshToken = jwtLoginProvider.generateRefreshToken(user.getId());
        createRefreshTokenPort.create(user.getId(), refreshToken);
        return new TokenPair(accessToken, refreshToken);
    }

    private List<SpaceInfo> getSpaceInfos(List<SpaceMember> spaceMembers) {
        List<SpaceInfo> spaceInfos = new ArrayList<>();
        for (SpaceMember spaceMember : spaceMembers) {
            Space space = loadSpacePort.loadSpaceById(spaceMember.getSpaceId()).orElseThrow(() -> new CustomException(SPACE_NOT_FOUND));
            spaceInfos.add(SpaceInfo.create(space, spaceMember));
        }
        return spaceInfos;
    }
}
