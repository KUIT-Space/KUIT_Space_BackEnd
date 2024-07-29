package space.space_spring.entity.enumStatus;

import lombok.Getter;

@Getter
public enum UserSignupType {
    LOCAL("local"),
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    private String signupType;

    UserSignupType(String signupType) {
        this.signupType = signupType;
    }
}
