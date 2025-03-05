package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.event.application.port.in.DeleteEventUseCase;
import space.space_spring.domain.event.application.port.out.UpdateEventParticipantPort;
import space.space_spring.domain.event.application.port.out.UpdateEventPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteEventService implements DeleteEventUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final UpdateEventPort updateEventPort;
    private final UpdateEventParticipantPort updateEventParticipantPort;

    @Override
    public boolean deleteEvent(Long spaceMemberId, Long eventId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        updateEventPort.delete(eventId);
        updateEventParticipantPort.deleteAllByEventId(eventId);

        return true;
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }

}
