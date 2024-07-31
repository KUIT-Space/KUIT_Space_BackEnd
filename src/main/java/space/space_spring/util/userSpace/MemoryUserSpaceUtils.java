package space.space_spring.util.userSpace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.SpaceDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.entity.enumStatus.UserSpaceAuth;
import space.space_spring.exception.UserSpaceException;

import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;

@Component
@RequiredArgsConstructor
public class MemoryUserSpaceUtils implements UserSpaceUtils {

    private final UserDao userDao;
    private final SpaceDao spaceDao;
    private final UserSpaceDao userSpaceDao;

    @Override
    @Transactional
    public Optional<UserSpace> isUserInSpace(Long userId, Long spaceId) {
        User userByUserId = userDao.findUserByUserId(userId);
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);

        return Optional.ofNullable(userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId)
                .orElseThrow(() -> new UserSpaceException(USER_IS_NOT_IN_SPACE)));
    }

    @Override
    public boolean isUserManager(Long userId, Long spaceId) {
        User userByUserId = userDao.findUserByUserId(userId);
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);

        // userId와 spaceId를 통해 UserSpace에서 권한 확인
        Optional<UserSpace> userSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId);

        String userSpaceAuth = userSpace.get().getUserSpaceAuth();
        return userSpaceAuth.equals(UserSpaceAuth.MANAGER.getAuth());
    }
}
