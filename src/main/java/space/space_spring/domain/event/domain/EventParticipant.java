package space.space_spring.domain.event.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class EventParticipant {

    private Long id;

    private Long eventId;

    private Long spaceMemberId;

    private BaseInfo baseInfo;

    private EventParticipant(Long id, Long eventId, Long spaceMemberId, BaseInfo baseInfo) {
        this.id = id;
        this.eventId = eventId;
        this.spaceMemberId = spaceMemberId;
        this.baseInfo = baseInfo;
    }

    public static EventParticipant create(Long id, Long eventId, Long spaceMemberId, BaseInfo baseInfo) {
        return new EventParticipant(id, eventId, spaceMemberId, baseInfo);
    }

    public static EventParticipant withoutId(Long eventId, Long spaceMemberId) {
        return new EventParticipant(null, eventId, spaceMemberId, BaseInfo.ofEmpty());
    }
}
