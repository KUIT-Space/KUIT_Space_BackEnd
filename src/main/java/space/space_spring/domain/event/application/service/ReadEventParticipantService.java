package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.event.application.port.in.ReadEventParticipantUseCase;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.domain.EventParticipantInfos;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class ReadEventParticipantService implements ReadEventParticipantUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventParticipantPort loadEventParticipantPort;

    @Override
    public EventParticipantInfos readEventParticipants(Long spaceMemberId, Long eventId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        EventParticipants participants = loadEventParticipantPort.loadByEventId(eventId);
        List<Long> participantIds = participants.getSpaceMemberIds();
        SpaceMembers spaceMemberInfos = SpaceMembers.of(loadSpaceMemberPort.loadAllById(participantIds));

        return EventParticipantInfos.create(spaceMemberInfos);
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }
}
