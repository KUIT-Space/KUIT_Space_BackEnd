package space.space_spring.dto.oauthDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.dto.pay.PayReceiveInfoDto;

@Getter
@AllArgsConstructor
public class KakaoLoginDto {

    private String email;

    private String nickname;

    public void saveKakaoLoginDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
