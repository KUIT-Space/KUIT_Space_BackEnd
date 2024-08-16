package space.space_spring.config.interceptorURL;

import lombok.Getter;

@Getter
public enum UserSpaceValidationInterceptorURL {
    //SPACE("/space/**"),
    TEST("/space/{spaceId}/test/**"),
//    VOICEROOM("/space/{spaceId}/voiceRoom/**"),
//    CHATROOM("/space/{spaceId}/chat/**")
    SPACE("/space/{spaceId}/**")
    ;

    private final String urlPattern;

    UserSpaceValidationInterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
