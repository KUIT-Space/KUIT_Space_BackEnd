package space.space_spring.domain.event.application.port.in;

public interface JoinEventUseCase {

    boolean joinEvent(Long spaceMemberId, Long eventId);

}
