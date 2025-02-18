package space.space_spring.domain.event.application.port.out;

import java.util.List;
import space.space_spring.domain.event.domain.Event;

public interface LoadEventPort {

    List<Event> loadEvents(Long spaceId);

}
