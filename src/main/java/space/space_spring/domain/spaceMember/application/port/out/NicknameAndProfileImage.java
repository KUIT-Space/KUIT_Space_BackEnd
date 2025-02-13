package space.space_spring.domain.spaceMember.application.port.out;

import lombok.Getter;

@Getter
public class NicknameAndProfileImage {

    private String nickname;

    private String profileImageUrl;

    private NicknameAndProfileImage(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static NicknameAndProfileImage of(String nickname, String profileImageUrl) {
        return new NicknameAndProfileImage(nickname, profileImageUrl);
    }
}
