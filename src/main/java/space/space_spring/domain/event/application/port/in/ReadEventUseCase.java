package space.space_spring.domain.event.application.port.in;

import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.Events;

import java.util.List;

public interface ReadEventUseCase {

    List<ResultOfEventPreviewInfo> readEvents(Long spaceMemberId);

    Event readEvent(Long spaceMemberId, Long eventId);
}
