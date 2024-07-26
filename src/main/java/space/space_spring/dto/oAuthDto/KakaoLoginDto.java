package space.space_spring.dto.oAuthDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
