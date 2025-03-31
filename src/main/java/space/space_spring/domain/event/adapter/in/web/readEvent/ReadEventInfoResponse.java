package space.space_spring.domain.event.adapter.in.web.readEvent;

import static space.space_spring.global.util.TimeConverter.convertUtcToKst;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipantInfo;
import space.space_spring.domain.event.domain.EventParticipantInfos;

@Getter
public class ReadEventInfoResponse {

    private Long id;

    private String name;

    private String image;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<EventParticipantInfo> participants;

    private ReadEventInfoResponse(Long id, String name, String image, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, List<EventParticipantInfo> participants) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
    }

    public static ReadEventInfoResponse create(Event event, EventParticipantInfos eventParticipantInfos) {
        return new ReadEventInfoResponse(
                event.getId(),
                event.getName(),
                event.getImage(),
                convertUtcToKst(event.getDate()),
                convertUtcToKst(event.getStartTime()),
                convertUtcToKst(event.getEndTime()),
                eventParticipantInfos.getParticipantInfos()
        );
    }
}
