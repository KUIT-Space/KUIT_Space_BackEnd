package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import space.space_spring.domain.event.application.port.in.ResultOfEventPreviewInfo;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.Events;

@Getter
public class ReadEventsResponse {

    private final List<EventInfoResponse> events;

    private ReadEventsResponse(List<EventInfoResponse> events) {
        this.events = Collections.unmodifiableList(events);
    }

    public static ReadEventsResponse create(List<ResultOfEventPreviewInfo> resultOfEventPreviewInfos) {
        List<EventInfoResponse> eventsResponse = new ArrayList<>();

        for (ResultOfEventPreviewInfo result : resultOfEventPreviewInfos) {
            eventsResponse.add(EventInfoResponse.create(result));
        }

        return new ReadEventsResponse(eventsResponse);
    }
}
