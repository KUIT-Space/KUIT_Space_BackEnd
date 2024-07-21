package space.space_spring.config;

public enum InterceptorURL {
    SPACE("/space/**"),
    TEST("/test/**"),
    SPACE_LIST_FOR_USER("/user/space");

    private final String urlPattern;

    InterceptorURL(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getUrlPattern() {
        return urlPattern;
    }
}
