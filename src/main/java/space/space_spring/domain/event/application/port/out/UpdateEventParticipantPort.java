package space.space_spring.domain.event.application.port.out;

public interface UpdateEventParticipantPort {

    void deleteAllByEventId(Long eventId);

}
