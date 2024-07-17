package space.space_spring.entity.enumStatus;

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
