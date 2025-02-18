package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.time.LocalDateTime;
import space.space_spring.domain.event.domain.Event;

public class ReadEventResponse {

    private Long id;

    private String name;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public ReadEventResponse(Event event) {

    }
}
