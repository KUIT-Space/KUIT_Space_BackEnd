package space.space_spring.argumentResolver.userSpace;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;

@Component
@RequiredArgsConstructor
public class UserSpaceAuthHandlerArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserSpaceDao userSpaceDao;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 일단 parameter의 return value type 을 검사하지는 X
        return parameter.hasParameterAnnotation(UserSpaceAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return
            userSpaceDao.findUserSpaceAuthById((Long)request.getAttribute("userSpaceId"));

    }
}
