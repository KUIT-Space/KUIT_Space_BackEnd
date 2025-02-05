package space.space_spring.domain.user.application.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import space.space_spring.domain.user.domain.User;

public interface OauthUseCase {

    String getAccessToken(String code) throws JsonProcessingException;

    User getUserInfo(String accessToken) throws JsonProcessingException;

    String signIn(User user);
}
