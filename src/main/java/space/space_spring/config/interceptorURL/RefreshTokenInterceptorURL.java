package space.space_spring.config.interceptorURL;

import lombok.Getter;

@Getter
public enum RefreshTokenInterceptorURL {

    ASK_NEW_ACCESS_TOKEN("/oauth/new-access-token");

    private final String urlPattern;

    RefreshTokenInterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
