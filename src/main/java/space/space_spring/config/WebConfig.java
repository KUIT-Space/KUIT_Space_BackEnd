package space.space_spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtLoginAuthInterceptor)
                .order(1)
                .addPathPatterns("/space/**", "/test/**");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jwtLoginAuthHandlerArgumentResolver);
    }
}
