package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.ALREADY_IN_EVENT;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.EVENT_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_EVENT_STATUS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.event.application.port.in.JoinEventUseCase;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinEventService implements JoinEventUseCase {

    private final LoadEventPort loadEventPort;
    private final CreateEventParticipantPort createEventParticipantPort;
    private final LoadEventParticipantPort loadEventParticipantPort;

    @Override
    public boolean joinEvent(Long spaceMemberId, Long eventId) {
        Event event = loadEventPort.loadEvent(eventId).orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
        validateEventStatus(event.getStartTime(), event.getEndTime());

        EventParticipant eventParticipant = EventParticipant.withoutId(eventId, spaceMemberId);
        validateDuplicateParticipant(eventParticipant);
        EventParticipant savedEventParticipant = createEventParticipantPort.createParticipant(eventParticipant);
        return savedEventParticipant != null;
    }

    private void validateEventStatus(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        if (now.isBefore(startTime) || now.isAfter(endTime)) {
            throw new CustomException(INVALID_EVENT_STATUS);
        }
    }

    private void validateDuplicateParticipant(EventParticipant eventParticipant) {
        EventParticipants eventParticipants = loadEventParticipantPort.loadByEventId(eventParticipant.getEventId());
        if (eventParticipants.isEmpty()) return;

        if (eventParticipants.isSpaceMemberIn(eventParticipant.getSpaceMemberId())) {
            throw new CustomException(ALREADY_IN_EVENT);
        }
    }

}
