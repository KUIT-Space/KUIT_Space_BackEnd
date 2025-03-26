package space.space_spring.domain.event.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.event.adapter.out.persistence.custom.EventParticipantRepositoryCustom;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface EventParticipantRepository extends JpaRepository<EventParticipantJpaEntity, Long>,
        EventParticipantRepositoryCustom {
    List<EventParticipantJpaEntity> findByEventAndStatusOrderByCreatedAtDesc(EventJpaEntity event, BaseStatusType status);

    Optional<EventParticipantJpaEntity> findByEventAndSpaceMember(EventJpaEntity event, SpaceMemberJpaEntity spaceMember);
}
