package space.space_spring.domain.event.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.JoinEventUseCase;
import space.space_spring.domain.event.application.port.out.CreateEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipant;

@Service
@RequiredArgsConstructor
public class JoinEventService implements JoinEventUseCase {

    private final CreateEventParticipantPort createEventParticipantPort;

    @Override
    public boolean joinEvent(Long spaceMemberId, Long eventId) {
        EventParticipant eventParticipant = EventParticipant.withoutId(eventId, spaceMemberId);
        EventParticipant savedEventParticipant = createEventParticipantPort.createParticipant(eventParticipant);
        return savedEventParticipant != null;
    }

}
