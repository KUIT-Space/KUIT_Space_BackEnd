package space.space_spring.domain.user.application.port.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import space.space_spring.domain.user.domain.User;

public interface DiscordOauthPort {

    String getAccessToken(String code) throws JsonProcessingException;

    User getUserInfo(String accessToken) throws JsonProcessingException;

}
