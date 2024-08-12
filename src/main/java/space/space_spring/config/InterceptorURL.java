package space.space_spring.config;

import lombok.Getter;

@Getter
public enum InterceptorURL {
    SPACE("/space/**"),
    TEST("/test/**"),
    SPACE_LIST_FOR_USER("/user/space-choice"),
    VOICE_ROOM("/voiceRoom/**"),
    USER_PROFILE_LIST("/user/profile");

    private final String urlPattern;

    InterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
