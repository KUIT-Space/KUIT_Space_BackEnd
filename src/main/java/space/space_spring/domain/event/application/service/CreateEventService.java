package space.space_spring.domain.event.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.event.application.port.in.CreateEventCommand;
import space.space_spring.domain.event.application.port.in.CreateEventUseCase;
import space.space_spring.domain.event.application.port.out.CreateEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateEventService implements CreateEventUseCase {

    private final CreateEventPort createEventPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public Long createEvent(Long spaceMemberId, CreateEventCommand createEventCommand) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        Event event = createEventCommand.toDomainEntity(spaceMember.getSpaceId());

        Event createdEvent = createEventPort.createEvent(event);
        return createdEvent.getId();
    }

}
