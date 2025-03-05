package space.space_spring.domain.spaceMember.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.spaceMember.application.port.out.PostCreatorNickname;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

public interface SpringDataSpaceMemberRepository extends JpaRepository<SpaceMemberJpaEntity, Long> {
    List<SpaceMemberJpaEntity> findBySpaceId(Long spaceId);

    Optional<SpaceMemberJpaEntity> findByUserId(Long userId);

    Optional<SpaceMemberJpaEntity> findBySpaceIdAndDiscordId(Long spaceId, Long discordId);


    Optional<SpaceMemberJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);

    @Query("SELECT new space.space_spring.domain.spaceMember.application.port.out.PostCreatorNickname(sm.id, sm.nickname) " +
            "FROM SpaceMemberJpaEntity sm " +
            "WHERE sm.id IN :spaceMemberIds " +
            "AND sm.status = :status")
    List<PostCreatorNickname> findNicknamesByIds(@Param("spaceMemberIds") List<Long> spaceMemberIds,
                                                 @Param("status") BaseStatusType status);
}
