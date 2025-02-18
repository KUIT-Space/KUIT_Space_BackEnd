package space.space_spring.domain.event.domain;

import lombok.Getter;

@Getter
public class EventParticipant {

    private Long id;

    private Long eventId;

    private Long spaceMemberId;

    private EventParticipant(Long id, Long eventId, Long spaceMemberId) {
        this.id = id;
        this.eventId = eventId;
        this.spaceMemberId = spaceMemberId;
    }

    public static EventParticipant create(Long id, Long eventId, Long spaceMemberId) {
        return new EventParticipant(id, eventId, spaceMemberId);
    }
}
