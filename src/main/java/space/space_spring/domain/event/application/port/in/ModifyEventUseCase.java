package space.space_spring.domain.event.application.port.in;

public interface ModifyEventUseCase {

    boolean addParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand);

    boolean deleteParticipants(Long spaceMemberId, Long eventId, UpdateEventParticipantCommand updateEventParticipantCommand);
}
