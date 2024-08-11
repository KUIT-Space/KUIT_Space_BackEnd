package space.space_spring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object Handler) throws Exception{

        Long userId = (Long) request.getAttribute("userId");

        // URL에서 spaceId 추출
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long spaceId = Long.parseLong((String) pathVariables.get("spaceId"));

        validateUserInSpace(spaceId,userId);
        return true;

    }

    private void validateUserInSpace(long spaceId,long userId){
        // userSpaceDao에서 검증
        User userByUserId = userDao.findUserByUserId(userId);
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);
        Optional.ofNullable(userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId)
                .orElseThrow(() -> new UserSpaceException(USER_IS_NOT_IN_SPACE)));

    }


}
