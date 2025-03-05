package space.space_spring.domain.event.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.event.adapter.out.persistence.custom.EventParticipantRepositoryCustom;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface EventParticipantRepository extends JpaRepository<EventParticipantJpaEntity, Long>,
        EventParticipantRepositoryCustom {
    List<EventParticipantJpaEntity> findByEventAndStatus(EventJpaEntity event, BaseStatusType statusType);
}
