package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipantInfo;
import space.space_spring.domain.event.domain.EventParticipantInfos;

@Getter
public class ReadEventInfoResponse extends EventInfoResponse {

    private List<EventParticipantInfo> participants;

    private ReadEventInfoResponse(Long id, String name, String image, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, List<EventParticipantInfo> participants) {
        super(id, name, image, date, startTime, endTime);
        this.participants = participants;
    }

    public static ReadEventInfoResponse create(Event event, EventParticipantInfos eventParticipantInfos) {
        return new ReadEventInfoResponse(
                event.getId(),
                event.getName(),
                event.getImage(),
                event.getDate(),
                event.getStartTime(),
                event.getEndTime(),
                eventParticipantInfos.getParticipantInfos()
        );
    }
}
