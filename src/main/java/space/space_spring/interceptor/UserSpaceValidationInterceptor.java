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
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.exception.CustomException;

import java.util.Map;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

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
            System.out.print("[DeBug]Interceptor pass By Annotation");
            return true;
        }
        Long userId = (Long) request.getAttribute("userId");

        // URL에서 spaceId 추출
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        //Long spaceId;
        try {
            Long spaceId = Long.parseLong((String) pathVariables.get("spaceId"));

            if(spaceId==null){
                throw new CustomException(SPACE_ID_PATHVARIABLE_NULL);
            }
            request.setAttribute("userSpaceId",getUserSpace(spaceId,userId));
            System.out.print("userSpaceID:"+getUserSpace(spaceId,userId));
            return true;

        }catch (NumberFormatException e){
            throw new CustomException(SPACE_ID_PATHVARIABLE_ERROR);

        }


    }

    private Long getUserSpace(long spaceId,long userId){
        // userSpaceDao에서 검증
        User userByUserId = userDao.findUserByUserId(userId);
        if(userByUserId==null){
            throw new CustomException(CANNOT_FIND_USER_ID);
        }
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);
        if(spaceBySpaceId==null){
            throw new CustomException(SPACE_NOT_FOUND);
        }
        Optional<UserSpace> userSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId);
        Optional.ofNullable(userSpace
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE)));
        return userSpace.get().getUserSpaceId();
    }


}
