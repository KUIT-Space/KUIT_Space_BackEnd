package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.entity.UserSpace;
import space.space_spring.entity.VoiceRoom;

import java.util.List;
import java.util.Optional;

public interface UserSpaceRepository extends JpaRepository<UserSpace, Long> {

    @Query("SELECT r FROM UserSpace r WHERE r.user.userId = :userId AND r.space.spaceId = :spaceId AND r.status = 'ACTIVE'")
    Optional<UserSpace> findUserSpaceByUserAndSpace(@Param("userId") Long userId, @Param("spaceId") Long spaceId);

}
