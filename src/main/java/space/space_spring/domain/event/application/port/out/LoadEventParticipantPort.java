package space.space_spring.domain.event.application.port.out;

import space.space_spring.domain.event.domain.EventParticipants;

public interface LoadEventParticipantPort {

    EventParticipants loadByEventId(Long eventId);

}
