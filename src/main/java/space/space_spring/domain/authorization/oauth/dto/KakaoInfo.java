package space.space_spring.domain.authorization.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoInfo {

    private String nickName;

    private String email;

}
