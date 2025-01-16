package space.space_spring.global.common.enumStatus;

import lombok.Getter;

@Getter
public enum UserSpaceAuth {
    MANAGER("manager"),
    NORMAL("normal");

    private String auth;

    UserSpaceAuth(String auth) {
        this.auth = auth;
    }
}
