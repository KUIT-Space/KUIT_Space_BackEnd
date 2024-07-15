package space.space_spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.space_spring.argument_resolver.JwtAuthHandlerArgumentResolver;
import space.space_spring.interceptor.JwtAuthInterceptor;
import space.space_spring.jwt.JwtProvider;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;
    private final JwtAuthHandlerArgumentResolver authHandlerArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .order(1)
                .addPathPatterns("/space/**", "/test/**");          // interceptor 적용되야하는 url enum으로 만들어서 여기에 달면 될듯
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authHandlerArgumentResolver);
    }
}
