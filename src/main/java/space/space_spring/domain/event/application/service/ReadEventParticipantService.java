package space.space_spring.domain.event.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.ReadEventParticipantUseCase;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipantInfos;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

@Service
@RequiredArgsConstructor
public class ReadEventParticipantService implements ReadEventParticipantUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventParticipantPort loadEventParticipantPort;

    @Override
    public EventParticipantInfos readEventParticipants(Long eventId) {
        EventParticipants participants = loadEventParticipantPort.loadByEventId(eventId);
        List<Long> participantIds = participants.getSpaceMemberIds();
        SpaceMembers spaceMemberInfos = SpaceMembers.of(loadSpaceMemberPort.loadAllById(participantIds));

        return EventParticipantInfos.create(spaceMemberInfos);
    }
}
