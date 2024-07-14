package space.space_spring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import space.space_spring.JwtProvider;
import space.space_spring.service.UserService;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserService userService;

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//    }
}
