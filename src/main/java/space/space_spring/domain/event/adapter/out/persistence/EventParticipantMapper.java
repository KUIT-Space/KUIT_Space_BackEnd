package space.space_spring.domain.event.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.event.domain.EventParticipant;

@Component
@RequiredArgsConstructor
public class EventParticipantMapper {

    public EventParticipant toDomainEntity(EventParticipantJpaEntity eventParticipantJpaEntity) {
        return EventParticipant.create(
                eventParticipantJpaEntity.getId(),
                eventParticipantJpaEntity.getEvent().getId(),
                eventParticipantJpaEntity.getSpaceMember().getId()
        );
    }
}
