package space.space_spring.domain.event.application.port.in;

public interface DeleteEventUseCase {

    boolean deleteEvent(Long spaceMemberId, Long eventId);

}
