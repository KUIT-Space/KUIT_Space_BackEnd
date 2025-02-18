package space.space_spring.domain.event.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.space.domain.SpaceJpaEntity;

@Component
@RequiredArgsConstructor
public class EventMapper {

    public EventJpaEntity toJpaEntity(SpaceJpaEntity spaceJpaEntity, Event event) {
        return EventJpaEntity.create(
                spaceJpaEntity,
                event.getName(),
                event.getDate(),
                event.getStartTime(),
                event.getEndTime()
        );
    }

    public Event toDomainEntity(EventJpaEntity eventJpaEntity) {
        return Event.create(
                eventJpaEntity.getId(),
                eventJpaEntity.getSpace().getId(),
                eventJpaEntity.getName(),
                eventJpaEntity.getDate(),
                eventJpaEntity.getStartTime(),
                eventJpaEntity.getEndTime()
        );
    }
}
