package space.space_spring.domain.event.domain;

import space.space_spring.global.util.NaturalNumber;

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

    public boolean isSpaceMemberIn(Long spaceMemberId) {
        for (EventParticipant eventParticipant : this.participants) {
            if (eventParticipant.getSpaceMemberId().equals(spaceMemberId)) return true;
        }
        return false;
    }

    public boolean isSpaceMemberNotIn(Long spaceMemberId) {
        for (EventParticipant eventParticipant : this.participants) {
            if (eventParticipant.getSpaceMemberId().equals(spaceMemberId)) return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return this.participants.isEmpty();
    }

    public NaturalNumber getTotalNumberOfParticipants() {
        return NaturalNumber.of(participants.size());
    }

    public boolean isSpaceMemberActive(Long spaceMemberId) {
        return participants.stream()
                .anyMatch(participant -> participant.getSpaceMemberId().equals(spaceMemberId) && participant.getBaseInfo().isActive());
    }

    public boolean isSpaceMemberInactive(Long spaceMemberId) {
        return participants.stream()
                .anyMatch(participant -> participant.getSpaceMemberId().equals(spaceMemberId) && !participant.getBaseInfo().isActive());
    }
}
