package space.space_spring.domain.event.adapter.out.persistence;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpaceMemberMapper;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

@RequiredArgsConstructor
@Repository
public class EventEventParticipantPersistenceAdapter implements LoadEventParticipantPort, CreateEventParticipantPort {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final EventMapper eventMapper;
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

    @Override
    public EventParticipant createParticipant(EventParticipant participant) {
        EventJpaEntity event = eventRepository.findById(participant.getEventId())
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
        SpaceMemberJpaEntity spaceMember = spaceMemberRepository.findById(participant.getSpaceMemberId())
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        EventParticipantJpaEntity savedParticipant = eventParticipantRepository.save(eventParticipantMapper.toJpaEntity(event, spaceMember));
        return eventParticipantMapper.toDomainEntity(savedParticipant);
    }

}
