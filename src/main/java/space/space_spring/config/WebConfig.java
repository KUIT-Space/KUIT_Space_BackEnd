package space.space_spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuthHandlerArgumentResolver;
import space.space_spring.interceptor.jwtLogin.JwtLoginAuthInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtLoginAuthInterceptor jwtLoginAuthInterceptor;

    private final JwtLoginAuthHandlerArgumentResolver jwtLoginAuthHandlerArgumentResolver;

    private static final String DEVELOP_FRONT_ADDRESS = "http://localhost:3000";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtLoginAuthInterceptor)
                .order(1)
                .addPathPatterns("/space/**", "/test/**", "/user/space");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jwtLoginAuthHandlerArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(DEVELOP_FRONT_ADDRESS)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("location")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
