package space.space_spring.domain.event.application.port.in;

import space.space_spring.domain.event.domain.EventParticipantInfos;

public interface ReadEventParticipantUseCase {

    EventParticipantInfos readEventParticipants(Long eventId);

}
