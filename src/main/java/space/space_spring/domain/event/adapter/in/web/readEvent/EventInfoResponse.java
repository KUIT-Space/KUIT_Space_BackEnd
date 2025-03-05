package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.event.domain.Event;

@Getter
@AllArgsConstructor
public class EventInfoResponse {

    private Long id;

    private String name;

    private String image;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static EventInfoResponse create(Event event) {
        return new EventInfoResponse(event.getId(), event.getName(), event.getImage(), event.getDate(), event.getStartTime(), event.getEndTime());
    }
}
