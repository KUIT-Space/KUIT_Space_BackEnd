package space.space_spring.domain.event.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.event.adapter.out.persistence.custom.EventRepositoryCustom;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface EventRepository extends JpaRepository<EventJpaEntity, Long>, EventRepositoryCustom {

    Optional<EventJpaEntity> findByIdAndStatus(Long id, BaseStatusType status);

    List<EventJpaEntity> findBySpaceAndStatus(SpaceJpaEntity space, BaseStatusType status);

}
