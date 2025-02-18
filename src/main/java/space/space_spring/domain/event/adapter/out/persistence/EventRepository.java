package space.space_spring.domain.event.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventJpaEntity, Long> {

    List<EventJpaEntity> findBySpaceId(Long spaceId);

}
