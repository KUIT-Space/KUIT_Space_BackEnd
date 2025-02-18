package space.space_spring.domain.event.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventJpaEntity, Long> {
}
