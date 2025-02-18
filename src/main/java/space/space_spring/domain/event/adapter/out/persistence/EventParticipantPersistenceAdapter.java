package space.space_spring.domain.event.adapter.out.persistence;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.global.exception.CustomException;

@RequiredArgsConstructor
@Repository
public class EventParticipantPersistenceAdapter implements LoadEventParticipantPort {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventParticipantMapper eventParticipantMapper;

    @Override
    public EventParticipants loadByEventId(Long eventId) {
        EventJpaEntity eventJpaEntity = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomException(EVENT_NOT_FOUND));
        List<EventParticipantJpaEntity> eventParticipantJpaEntities = eventParticipantRepository.findByEvent(eventJpaEntity);

        List<EventParticipant> participants = new ArrayList<>();
        for (EventParticipantJpaEntity e : eventParticipantJpaEntities) {
            participants.add(eventParticipantMapper.toDomainEntity(e));
        }

        return EventParticipants.create(participants);
    }
}
