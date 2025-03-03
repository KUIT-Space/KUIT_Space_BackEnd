package space.space_spring.domain.event.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EventParticipants {

    private final List<EventParticipant> participants;

    private EventParticipants(List<EventParticipant> participants) {
        this.participants = Collections.unmodifiableList(Objects.requireNonNull(participants, "event participants는 null일 수 없습니다."));
    }

    public static EventParticipants create(List<EventParticipant> participants) {
        return new EventParticipants(participants);
    }

    public List<Long> getSpaceMemberIds() {
        List<Long> ids = new ArrayList<>();

        for (EventParticipant eventParticipant : this.participants) {
            ids.add(eventParticipant.getSpaceMemberId());
        }

        return Collections.unmodifiableList(ids);
    }

    public boolean isAlreadyIn(Long spaceMemberId) {
        for (EventParticipant eventParticipant : this.participants) {
            if (eventParticipant.getSpaceMemberId().equals(spaceMemberId)) return true;
        }
        return false;
    }

}
