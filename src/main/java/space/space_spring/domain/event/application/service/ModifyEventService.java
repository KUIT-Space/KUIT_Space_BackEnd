package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.event.application.port.in.ModifyEventUseCase;
import space.space_spring.domain.event.application.port.in.UpdateEventParticipantCommand;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventPort;
import space.space_spring.domain.event.application.port.out.UpdateEventParticipantPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional
public class ModifyEventService implements ModifyEventUseCase {

    private final LoadEventPort loadEventPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventParticipantPort loadEventParticipantPort;
    private final CreateEventParticipantPort createEventParticipantPort;
    private final UpdateEventParticipantPort updateEventParticipantPort;

    @Override
    public boolean addParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        Event event = loadEventPort.loadEvent(eventId).orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
        validateEventStatus(event.getStartTime(), event.getEndTime());

        EventParticipants savedEventParticipants = loadEventParticipantPort.loadByEventId(eventId);
        List<Long> spaceMemberIds = updateEventParticipantCommand.getSpaceMemberIds();
        for (Long id : spaceMemberIds) {
            if (!savedEventParticipants.isEmpty() && savedEventParticipants.isSpaceMemberIn(id)) throw new CustomException(ALREADY_IN_EVENT);

            EventParticipant eventParticipant = EventParticipant.withoutId(eventId, id);
            createEventParticipantPort.createParticipant(eventParticipant);
        }
        return true;
    }

    @Override
    public boolean deleteParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        Event event = loadEventPort.loadEvent(eventId).orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
        validateEventStatus(event.getStartTime(), event.getEndTime());

        EventParticipants savedEventParticipants = loadEventParticipantPort.loadByEventId(eventId);
        List<Long> spaceMemberIds = updateEventParticipantCommand.getSpaceMemberIds();
        for (Long id : spaceMemberIds) {
            if (!savedEventParticipants.isEmpty() && savedEventParticipants.isSpaceMemberNotIn(id)) throw new CustomException(ALREADY_NOT_IN_EVENT);

            updateEventParticipantPort.deleteParticipant(eventId, id);
        }
        return true;
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }

    private void validateEventStatus(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        if (now.isBefore(startTime) || now.isAfter(endTime)) {
            throw new CustomException(INVALID_EVENT_STATUS);
        }
    }
}
