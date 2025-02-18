package space.space_spring.domain.event.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

public interface EventRepository extends JpaRepository<EventJpaEntity, Long> {

    List<EventJpaEntity> findBySpace(SpaceJpaEntity space);

}
