package space.space_spring.domain.event.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Events {

    private final List<Event> events;

    private Events(List<Event> events) {
        this.events = Collections.unmodifiableList(Objects.requireNonNull(events, "events는 null일 수 없습니다."));
    }

    public static Events create(List<Event> events) {
        return new Events(events);
    }

    public List<Event> getEvents() {
        return List.copyOf(events);
    }
}
