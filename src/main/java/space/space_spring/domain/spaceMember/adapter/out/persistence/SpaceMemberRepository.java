package space.space_spring.domain.spaceMember.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.spaceMember.adapter.out.persistence.custom.SpaceMemberRepositoryCustom;
import space.space_spring.domain.spaceMember.application.port.out.PostCreatorNickname;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

public interface SpaceMemberRepository extends JpaRepository<SpaceMemberJpaEntity, Long>, SpaceMemberRepositoryCustom {
    List<SpaceMemberJpaEntity> findBySpaceId(Long spaceId);

    List<SpaceMemberJpaEntity> findByUserId(Long userId);

    Optional<SpaceMemberJpaEntity> findBySpaceIdAndDiscordIdAndStatus(Long spaceId, Long discordId,BaseStatusType status);


    Optional<SpaceMemberJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);

}
