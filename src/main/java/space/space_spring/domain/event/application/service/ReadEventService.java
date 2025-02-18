package space.space_spring.domain.event.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.ReadEventUseCase;
import space.space_spring.domain.event.application.port.out.LoadEventPort;
import space.space_spring.domain.event.domain.Events;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Service
@RequiredArgsConstructor
public class ReadEventService implements ReadEventUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventPort loadEventPort;

    @Override
    public Events readEvents(Long spaceMemberId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        return Events.create(loadEventPort.loadEvents(spaceMember.getSpaceId()));
    }

}
