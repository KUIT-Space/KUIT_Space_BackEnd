package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class ModifyEventService implements ModifyEventUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventParticipantPort loadEventParticipantPort;
    private final CreateEventParticipantPort createEventParticipantPort;
    private final UpdateEventParticipantPort updateEventParticipantPort;

    @Override
    public boolean addParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        EventParticipants savedEventParticipants = loadEventParticipantPort.loadAllByEventId(eventId);
        List<Long> spaceMemberIds = updateEventParticipantCommand.getSpaceMemberIds();

        for (Long id : spaceMemberIds) {
            if (savedEventParticipants.isSpaceMemberActive(id)) { // 이미 참여 중
                throw new CustomException(ALREADY_IN_EVENT);
            } else if (savedEventParticipants.isSpaceMemberInactive(id)) { // 이전에 참여했던 내역 있음
                updateEventParticipantPort.activateParticipant(eventId, id);
            } else { // 참여 내역 없음
                EventParticipant eventParticipant = EventParticipant.withoutId(eventId, id);
                createEventParticipantPort.createParticipant(eventParticipant);
            }
        }
        return true;
    }

    @Override
    public boolean deleteParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        EventParticipants savedEventParticipants = loadEventParticipantPort.loadAllByEventId(eventId);
        List<Long> spaceMemberIds = updateEventParticipantCommand.getSpaceMemberIds();

        for (Long id : spaceMemberIds) {
            if (savedEventParticipants.isSpaceMemberNotIn(id)) { // 참여 내역 없음
                throw new CustomException(ALREADY_NOT_IN_EVENT);
            } else if (savedEventParticipants.isSpaceMemberActive(id)) { // 이전에 참여했던 내역 있음
                updateEventParticipantPort.deleteParticipant(eventId, id);
            } else { // 이전 참여 내역이 이미 삭제됨
                throw new CustomException(ALREADY_NOT_IN_EVENT);
            }
        }
        return true;
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }
}
