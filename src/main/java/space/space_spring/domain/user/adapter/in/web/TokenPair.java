package space.space_spring.domain.user.adapter.in.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenPair {

    private final String accessToken;

    private final String refreshToken;

}
