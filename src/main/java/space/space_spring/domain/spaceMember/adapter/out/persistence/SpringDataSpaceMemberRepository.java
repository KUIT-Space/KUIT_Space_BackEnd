package space.space_spring.domain.spaceMember.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

import java.util.List;
import java.util.Optional;

public interface SpringDataSpaceMemberRepository extends JpaRepository<SpaceMemberJpaEntity, Long> {
    List<SpaceMemberJpaEntity> findBySpaceId(Long spaceId);

    Optional<SpaceMemberJpaEntity> findByUserId(Long userId);

    Optional<SpaceMemberJpaEntity> findBySpaceIdAndDiscordId(Long SpaceId,Long discordId);


}
