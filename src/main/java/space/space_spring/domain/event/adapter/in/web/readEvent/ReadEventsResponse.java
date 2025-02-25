package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.Events;

public class ReadEventsResponse {

    private final List<EventInfoResponse> events;

    private ReadEventsResponse(List<EventInfoResponse> events) {
        this.events = Collections.unmodifiableList(events);
    }

    public static ReadEventsResponse create(Events events) {
        List<EventInfoResponse> eventsResponse = new ArrayList<>();

        for (Event event : events.getEvents()) {
            eventsResponse.add(EventInfoResponse.create(event));
        }

        return new ReadEventsResponse(eventsResponse);
    }
}
