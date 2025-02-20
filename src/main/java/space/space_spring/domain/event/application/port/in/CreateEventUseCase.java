package space.space_spring.domain.event.application.port.in;

public interface CreateEventUseCase {

    Long createEvent(Long spaceMemberId, CreateEventCommand createEventCommand);

}
