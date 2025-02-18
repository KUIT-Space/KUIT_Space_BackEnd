package space.space_spring.domain.event.application.port.out;

import space.space_spring.domain.event.domain.Event;

public interface CreateEventPort {

    Event createEvent(Event event);

}
