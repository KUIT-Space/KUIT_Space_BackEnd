package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.Events;

public class ReadEventsResponse {

    private final List<ReadEventResponse> events;

    private ReadEventsResponse(List<ReadEventResponse> events) {
        this.events = Collections.unmodifiableList(events);
    }

    public static ReadEventsResponse create(Events events) {
        List<ReadEventResponse> eventsResponse = new ArrayList<>();

        for (Event event : events.getEvents()) {
            eventsResponse.add(new ReadEventResponse(event));
        }

        return new ReadEventsResponse(eventsResponse);
    }
}
