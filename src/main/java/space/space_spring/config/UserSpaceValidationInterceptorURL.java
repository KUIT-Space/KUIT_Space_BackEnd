package space.space_spring.config;

import lombok.Getter;

@Getter
public enum UserSpaceValidationInterceptorURL {
    //SPACE("/space/**"),
    TEST("/space/{spaceId}/test"),
    ;

    private final String urlPattern;

    UserSpaceValidationInterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
