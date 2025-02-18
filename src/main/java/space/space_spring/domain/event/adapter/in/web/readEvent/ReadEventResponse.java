package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import space.space_spring.domain.event.domain.Event;

@AllArgsConstructor
public class ReadEventResponse {

    private Long id;

    private String name;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static ReadEventResponse create(Event event) {
        return new ReadEventResponse(event.getId(), event.getName(), event.getDate(), event.getStartTime(), event.getEndTime());
    }
}
