package space.space_spring.domain.event.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipantJpaEntity, Long> {
    List<EventParticipantJpaEntity> findByEvent(EventJpaEntity event);
}
