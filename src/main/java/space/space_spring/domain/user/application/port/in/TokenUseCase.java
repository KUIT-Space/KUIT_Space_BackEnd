package space.space_spring.domain.user.application.port.in;

import space.space_spring.domain.user.adapter.in.web.TokenPair;

public interface TokenUseCase {

    TokenPair updateTokenPair(TokenPair expiredTokenPair);

}
