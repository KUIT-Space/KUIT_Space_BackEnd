package space.space_spring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import space.space_spring.argumentResolver.userSpace.CheckUserSpace;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.exception.UserSpaceException;

import java.util.Map;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;

@Component
@RequiredArgsConstructor
public class UserSpaceValidationInterceptor implements HandlerInterceptor {

    private final UserSpaceDao userSpaceDao;
    private final UserDao userDao;
    private final SpaceDao spaceDao;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{

        // @CheckUserSpace(require=false)인 경우 검증하지 않음
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CheckUserSpace methodAnnotation= handlerMethod.getMethodAnnotation(CheckUserSpace.class);
        if(methodAnnotation!=null && !methodAnnotation.required()){
            return true;
        }
        Long userId = (Long) request.getAttribute("userId");

        // URL에서 spaceId 추출
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long spaceId = Long.parseLong((String) pathVariables.get("spaceId"));

        request.setAttribute("userSpaceId",getUserSpace(spaceId,userId));
        System.out.print("userSpaceID:"+getUserSpace(spaceId,userId));
        return true;

    }

    private Long getUserSpace(long spaceId,long userId){
        // userSpaceDao에서 검증
        User userByUserId = userDao.findUserByUserId(userId);
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);
        Optional<UserSpace> userSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId);
        Optional.ofNullable(userSpace
                .orElseThrow(() -> new UserSpaceException(USER_IS_NOT_IN_SPACE)));
        return userSpace.get().getUserSpaceId();
    }


}
