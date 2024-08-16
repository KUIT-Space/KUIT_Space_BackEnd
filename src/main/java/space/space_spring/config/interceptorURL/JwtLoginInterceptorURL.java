package space.space_spring.config.interceptorURL;

import lombok.Getter;

@Getter
public enum JwtLoginInterceptorURL {
    SPACE("/space/**"),
    TEST("/test/**"),
    SPACE_LIST_FOR_USER("/user/space-choice"),
    VOICE_ROOM("/voiceRoom/**"),
    USER_PROFILE_LIST("/user/profile");

    private final String urlPattern;

    JwtLoginInterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
