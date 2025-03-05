package space.space_spring.domain.event.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

public class EventParticipantInfos {

    private final List<EventParticipantInfo> participantInfos;

    private EventParticipantInfos(List<EventParticipantInfo> participantInfos) {
        this.participantInfos = Collections.unmodifiableList(participantInfos);
    }

    public static EventParticipantInfos create(SpaceMembers spaceMemberInfos) {
        List<EventParticipantInfo> participantInfos = spaceMemberInfos.toStream()
                .map(spaceMember -> new EventParticipantInfo(spaceMember.getId(), spaceMember.getNickname(), spaceMember.getProfileImageUrl()))
                .collect(Collectors.toList());

        return new EventParticipantInfos(participantInfos);
    }

    public static EventParticipantInfos createEmpty() {
        return new EventParticipantInfos(List.of());
    }

    public List<EventParticipantInfo> getParticipantInfos() {
        return List.copyOf(this.participantInfos);
    }
}
