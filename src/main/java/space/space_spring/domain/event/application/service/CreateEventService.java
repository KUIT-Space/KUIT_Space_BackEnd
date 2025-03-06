package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.event.application.port.in.CreateEventCommand;
import space.space_spring.domain.event.application.port.in.CreateEventUseCase;
import space.space_spring.domain.event.application.port.out.CreateEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateEventService implements CreateEventUseCase {

    private final CreateEventPort createEventPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public Long createEvent(Long spaceMemberId, CreateEventCommand createEventCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);
        Event event = createEventCommand.toDomainEntity(spaceMember.getSpaceId());

        Event createdEvent = createEventPort.createEvent(event);
        return createdEvent.getId();
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }

}
