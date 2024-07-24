package space.space_spring.util.userSpace;

import space.space_spring.entity.UserSpace;

import java.util.Optional;

public interface UserSpaceUtils {

    /**
     * 특정 유저가 특정 스페이스에 속한 유저가 맞는가?
     * 맞다면, 이 유저는 스페이스 내에서 어떤 역할을 가지는가?
     */

    Optional<UserSpace> isUserInSpace(Long userId, Long spaceId);


}
