package space.space_spring.domain.event.application.port.out;

import space.space_spring.domain.event.domain.EventParticipant;

public interface CreateEventParticipantPort {
    EventParticipant createParticipant(EventParticipant participant);
}
