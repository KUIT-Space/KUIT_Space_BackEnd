package space.space_spring.domain.event.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Event {

    private Long id;

    private Long spaceId;

    private String name;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    private Event(Long id, Long spaceId, String name, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.spaceId = spaceId;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Event create(Long id, Long spaceId, String name, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return Event.builder()
                .id(id)
                .spaceId(spaceId)
                .name(name)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public static Event withoutId(Long spaceId, String name, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return Event.builder()
                .spaceId(spaceId)
                .name(name)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

}
