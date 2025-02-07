package space.space_spring.domain.user.application.port.in;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface OauthUseCase {

    String signInWithDiscord(String code) throws JsonProcessingException;

}
