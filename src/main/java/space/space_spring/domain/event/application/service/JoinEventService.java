package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.ALREADY_IN_EVENT;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.JoinEventUseCase;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class JoinEventService implements JoinEventUseCase {

    private final CreateEventParticipantPort createEventParticipantPort;
    private final LoadEventParticipantPort loadEventParticipantPort;

    @Override
    public boolean joinEvent(Long spaceMemberId, Long eventId) {
        EventParticipant eventParticipant = EventParticipant.withoutId(eventId, spaceMemberId);
        validateDuplicateParticipant(eventParticipant);
        EventParticipant savedEventParticipant = createEventParticipantPort.createParticipant(eventParticipant);
        return savedEventParticipant != null;
    }

    private void validateDuplicateParticipant(EventParticipant eventParticipant) {
        EventParticipants eventParticipants = loadEventParticipantPort.loadByEventId(eventParticipant.getEventId());
        if (eventParticipants.isSpaceMemberIn(eventParticipant.getSpaceMemberId())) {
            throw new CustomException(ALREADY_IN_EVENT);
        }
    }

}
