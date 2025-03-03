package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.ReadEventUseCase;
import space.space_spring.domain.event.application.port.out.LoadEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.Events;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class ReadEventService implements ReadEventUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventPort loadEventPort;

    @Override
    public Events readEvents(Long spaceMemberId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);
        return Events.create(loadEventPort.loadEvents(spaceMember.getSpaceId()));
    }

    @Override
    public Event readEvent(Long spaceMemberId, Long eventId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);
        return loadEventPort.loadEvent(eventId).orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }

}
