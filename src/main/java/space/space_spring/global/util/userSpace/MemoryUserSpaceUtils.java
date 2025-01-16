package space.space_spring.global.util.userSpace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.space.repository.SpaceDao;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.global.common.enumStatus.UserSpaceAuth;
import space.space_spring.global.exception.CustomException;

import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.USER_IS_ALREADY_IN_SPACE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;

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
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE)));
    }

    @Override
    public boolean isUserManager(Long userId, Long spaceId) {
        User userByUserId = userDao.findUserByUserId(userId);
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);

        // userId와 spaceId를 통해 UserSpace에서 권한 확인
        Optional<UserSpace> userSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId);

        if (userSpace.isPresent()) {
            String userSpaceAuth = userSpace.get().getUserSpaceAuth();
            return userSpaceAuth.equals(UserSpaceAuth.MANAGER.getAuth());
        }
        return false;
    }

    @Override
    public void isUserAlreadySpaceMember(Long userId, Long spaceId) {
        User userByUserId = userDao.findUserByUserId(userId);
        Space spaceBySpaceId = spaceDao.findSpaceBySpaceId(spaceId);

        // 해당 유저가 스페이스에 가입되어 있는지를 확인
        Optional<UserSpace> userSpaceByUserAndSpace = userSpaceDao.findUserSpaceByUserAndSpace(userByUserId, spaceBySpaceId);

        if (userSpaceByUserAndSpace.isPresent()) {
            throw new CustomException(USER_IS_ALREADY_IN_SPACE);
        }
    }
}
