package space.space_spring.global.config.interceptorURL;

import lombok.Getter;

@Getter
public enum JwtLoginInterceptorURL {
    SPACE("/space/**"),
    TEST("/test/**"),
    ;

    private final String urlPattern;

    JwtLoginInterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
