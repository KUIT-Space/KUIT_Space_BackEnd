package space.space_spring.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.space_spring.global.argumentResolver.jwtLogin.JwtAuthorizationArgumentResolver;
import space.space_spring.global.argumentResolver.jwtLogin.JwtSpaceIdArgumentResolver;
import space.space_spring.global.config.interceptorURL.JwtLoginInterceptorURL;
import space.space_spring.global.interceptor.jwtLogin.JwtAuthInterceptor;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    private final JwtAuthorizationArgumentResolver jwtAuthorizationArgumentResolver;

    private final JwtSpaceIdArgumentResolver jwtSpaceIdArgumentResolver;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration =
                registry.addInterceptor(jwtAuthInterceptor)
                .order(1);

        for (JwtLoginInterceptorURL interceptorURL : JwtLoginInterceptorURL.values()) {
            registration.addPathPatterns(interceptorURL.getUrlPattern());
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jwtAuthorizationArgumentResolver);
        argumentResolvers.add(jwtSpaceIdArgumentResolver);
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
