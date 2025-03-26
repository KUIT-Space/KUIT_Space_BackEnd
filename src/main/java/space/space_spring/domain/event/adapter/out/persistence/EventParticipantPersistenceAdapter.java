package space.space_spring.domain.event.adapter.out.persistence;

import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.application.port.out.UpdateEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

@RequiredArgsConstructor
@Repository
public class EventParticipantPersistenceAdapter implements LoadEventParticipantPort, CreateEventParticipantPort,
        UpdateEventParticipantPort {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final EventParticipantMapper eventParticipantMapper;

    @Override
    public EventParticipants loadByEventId(Long eventId) {
        EventJpaEntity eventJpaEntity = eventRepository.findByIdAndStatus(eventId, ACTIVE).orElseThrow(
                () -> new CustomException(EVENT_NOT_FOUND));
        List<EventParticipantJpaEntity> eventParticipantJpaEntities = eventParticipantRepository.findByEventAndStatusOrderByCreatedAtDesc(eventJpaEntity, ACTIVE);

        List<EventParticipant> participants = new ArrayList<>();
        for (EventParticipantJpaEntity e : eventParticipantJpaEntities) {
            participants.add(eventParticipantMapper.toDomainEntity(e));
        }

        return EventParticipants.create(participants);
    }

    @Override
    public EventParticipant createParticipant(EventParticipant participant) {
        EventJpaEntity event = eventRepository.findByIdAndStatus(participant.getEventId(), ACTIVE)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findById(participant.getSpaceMemberId())
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        if (eventParticipantRepository.existsByEventAndSpaceMember(event, spaceMember)) {
            throw new CustomException(ALREADY_IN_EVENT);
        }

        EventParticipantJpaEntity savedParticipant = eventParticipantRepository.save(eventParticipantMapper.toJpaEntity(event, spaceMember));
        return eventParticipantMapper.toDomainEntity(savedParticipant);
    }

    @Override
    public void deleteAllByEventId(Long eventId) {
        EventJpaEntity eventJpaEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));

        if (eventJpaEntity.isNotActive()) throw new CustomException(EVENT_NOT_FOUND);

        eventParticipantRepository.deleteAllByEvent(eventJpaEntity);
    }

    @Override
    public void deleteParticipant(Long eventId, Long spaceMemberId) {
        EventJpaEntity eventJpaEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));

        if (eventJpaEntity.isNotActive()) throw new CustomException(EVENT_NOT_FOUND);

        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(spaceMemberId)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));
        EventParticipantJpaEntity eventParticipantJpaEntity = eventParticipantRepository.findByEventAndSpaceMember(eventJpaEntity, spaceMemberJpaEntity)
                .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

        if (eventParticipantJpaEntity.isNotActive()) throw new CustomException(PARTICIPANT_NOT_FOUND);

        eventParticipantRepository.softDelete(eventParticipantJpaEntity);
    }
}
