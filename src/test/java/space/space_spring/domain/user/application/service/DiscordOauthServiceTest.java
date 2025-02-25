package space.space_spring.domain.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import space.space_spring.domain.user.application.port.out.DiscordOauthPort;
import space.space_spring.domain.user.application.port.out.LoadUserPort;
import space.space_spring.domain.user.domain.User;

public class DiscordOauthServiceTest {

    private DiscordOauthPort discordOauthPort;
    private LoadUserPort loadUserPort;
    private LoadSpaceMemberPort loadSpaceMemberPort;
    private CreateRefreshTokenPort createRefreshTokenPort;
    private JwtLoginProvider jwtLoginProvider;
    private DiscordOauthService discordOauthService;

    private static User user;
    private static SpaceMember spaceMember;
    private static final String code = "";
    private static final String accessToken = "";

    @BeforeEach
    void setup() throws JsonProcessingException {
        discordOauthPort = Mockito.mock(DiscordOauthPort.class);
        loadUserPort = Mockito.mock(LoadUserPort.class);
        loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
        createRefreshTokenPort = Mockito.mock(CreateRefreshTokenPort.class);
        jwtLoginProvider = Mockito.mock(JwtLoginProvider.class);
        discordOauthService = new DiscordOauthService(discordOauthPort, loadUserPort, loadSpaceMemberPort, createRefreshTokenPort, jwtLoginProvider);

        user = User.create(0L, 0L);
        spaceMember = SpaceMember.create(0L, 0L, 0L, 0L, "nickname", "profileImageUrl", false);

        Mockito.when(discordOauthPort.getAccessToken(code)).thenReturn(accessToken);
        Mockito.when(discordOauthPort.getUserInfo(accessToken)).thenReturn(user);
    }

    @Test
    @DisplayName("KUIT Space에 저장되어있는 유저의 경우 로그인을 수행하며 token을 발급한다.")
    void signIn() throws JsonProcessingException {
        //given
        Mockito.when(loadUserPort.loadUserByDiscordId(user.getDiscordId())).thenReturn(Optional.ofNullable(user));
        Mockito.when(loadSpaceMemberPort.loadByUserId(user.getId())).thenReturn(spaceMember);
        Mockito.when(jwtLoginProvider.generateToken(user.getId(), spaceMember.getId(), TokenType.ACCESS)).thenReturn("result-access-token");
        Mockito.when(jwtLoginProvider.generateToken(user.getId(), spaceMember.getId(), TokenType.REFRESH)).thenReturn("result-refresh-token");

        //when
        TokenPair tokenPair = discordOauthService.signIn(code);

        //then
        assertThat(tokenPair.getAccessToken()).isEqualTo("result-access-token");
        assertThat(tokenPair.getRefreshToken()).isEqualTo("result-refresh-token");
    }

    @Test
    @DisplayName("KUIT Space에 저장되어있지 않은 유저의 경우 로그인을 수행하지 않으며 token 또한 발급하지 않는다.")
    void signIn_fail() throws JsonProcessingException {
        // given
        Mockito.when(loadUserPort.loadUserByDiscordId(user.getDiscordId())).thenReturn(Optional.empty());

        // when
        TokenPair tokenPair = discordOauthService.signIn(code);

        // then
        assertThat(tokenPair).isNull();
    }
}
