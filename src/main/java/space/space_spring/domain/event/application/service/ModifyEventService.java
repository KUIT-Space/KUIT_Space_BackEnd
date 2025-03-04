package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.ModifyEventUseCase;
import space.space_spring.domain.event.application.port.in.UpdateEventParticipantCommand;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.application.port.out.UpdateEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class ModifyEventService implements ModifyEventUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventParticipantPort loadEventParticipantPort;
    private final CreateEventParticipantPort createEventParticipantPort;
    private final UpdateEventParticipantPort updateEventParticipantPort;

    @Override
    public boolean addParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        EventParticipants savedEventParticipants = loadEventParticipantPort.loadByEventId(eventId);
        List<Long> spaceMemberIds = updateEventParticipantCommand.getSpaceMemberIds();
        for (Long id : spaceMemberIds) {
            if (savedEventParticipants.isSpaceMemberIn(id)) throw new CustomException(ALREADY_IN_EVENT);

            EventParticipant eventParticipant = EventParticipant.withoutId(eventId, id);
            createEventParticipantPort.createParticipant(eventParticipant);
        }
        return true;
    }

    @Override
    public boolean deleteParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        EventParticipants savedEventParticipants = loadEventParticipantPort.loadByEventId(eventId);
        List<Long> spaceMemberIds = updateEventParticipantCommand.getSpaceMemberIds();
        for (Long id : spaceMemberIds) {
            if (savedEventParticipants.isSpaceMemberNotIn(id)) throw new CustomException(ALREADY_NOT_IN_EVENT);

            updateEventParticipantPort.deleteParticipant(eventId, id);
        }
        return true;
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }
}
