package space.space_spring.domain.authorization.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenPairDTO {

    private String refreshToken;
    private String accessToken;
}
