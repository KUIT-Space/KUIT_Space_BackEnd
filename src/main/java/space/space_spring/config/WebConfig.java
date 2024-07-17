package space.space_spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuthHandlerArgumentResolver;
import space.space_spring.argument_resolver.jwtUserSpace.JwtUserSpaceAuthHandlerArgumentResolver;
import space.space_spring.interceptor.jwtLogin.JwtLoginAuthInterceptor;
import space.space_spring.interceptor.jwtUserSpace.JwtUserSpaceAuthInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtLoginAuthInterceptor jwtLoginAuthInterceptor;
    private final JwtUserSpaceAuthInterceptor jwtUserSpaceAuthInterceptor;

    private final JwtLoginAuthHandlerArgumentResolver jwtLoginAuthHandlerArgumentResolver;
    private final JwtUserSpaceAuthHandlerArgumentResolver jwtUserSpaceAuthHandlerArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtLoginAuthInterceptor)
                .order(1)
                .addPathPatterns("/space/**", "/test/**");

        registry.addInterceptor(jwtUserSpaceAuthInterceptor)
                .order(2)
                .addPathPatterns("/test111/**");          // interceptor 적용되야하는 url enum으로 만들어서 여기에 달면 될듯
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jwtLoginAuthHandlerArgumentResolver);
        argumentResolvers.add(jwtUserSpaceAuthHandlerArgumentResolver);
    }
}
