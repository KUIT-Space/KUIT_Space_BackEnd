package space.space_spring.domain.user.application.port.in;


import com.fasterxml.jackson.core.JsonProcessingException;
import space.space_spring.domain.user.adapter.in.web.TokenPair;

public interface OauthUseCase {

    TokenPair signIn(String code) throws JsonProcessingException;

}
