package space.space_spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuthHandlerArgumentResolver;
import space.space_spring.argumentResolver.userSpace.UserSpaceAuth;
import space.space_spring.argumentResolver.userSpace.UserSpaceAuthHandlerArgumentResolver;
import space.space_spring.argumentResolver.userSpace.UserSpaceIdHandlerArgumentResolver;
import space.space_spring.interceptor.UserSpaceValidationInterceptor;
import space.space_spring.interceptor.jwtLogin.JwtLoginAuthInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtLoginAuthInterceptor jwtLoginAuthInterceptor;

    private final JwtLoginAuthHandlerArgumentResolver jwtLoginAuthHandlerArgumentResolver;
    private final UserSpaceIdHandlerArgumentResolver userSpaceIdHandlerArgumentResolver;
    private final UserSpaceAuthHandlerArgumentResolver userSpaceAuthHandlerArgumentResolver;
    private final UserSpaceValidationInterceptor userSpaceValidationInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration =
                registry.addInterceptor(jwtLoginAuthInterceptor)
                .order(1);

        for (InterceptorURL interceptorURL : InterceptorURL.values()) {
            registration.addPathPatterns(interceptorURL.getUrlPattern());
        }

        InterceptorRegistration userSpaceRegistration =
                registry.addInterceptor(userSpaceValidationInterceptor)
                        .order(2);
        for(UserSpaceValidationInterceptorURL url:UserSpaceValidationInterceptorURL.values()) {
            userSpaceRegistration.addPathPatterns(url.getUrlPattern());
        }


    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jwtLoginAuthHandlerArgumentResolver);
        argumentResolvers.add(userSpaceIdHandlerArgumentResolver);
        argumentResolvers.add(userSpaceAuthHandlerArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000/", "http://localhost:5173/", "https://localhost:5173/",
                        "http://localhost:5173/KUIT-Space-Front/", "https://localhost:5173/KUIT-Space-Front/",
                        "https://kuit-space.github.io/", "https://kuit-space-front.vercel.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .exposedHeaders("location", "Authorization")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
