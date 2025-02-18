package space.space_spring.domain.event.application.port.in;

import space.space_spring.domain.event.domain.Events;

public interface ReadEventUseCase {

    Events readEvents(Long spaceMemberId);

}
