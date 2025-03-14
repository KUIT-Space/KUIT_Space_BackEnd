package space.space_spring.domain.event.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.event.application.port.in.ReadEventUseCase;
import space.space_spring.domain.event.application.port.in.ResultOfEventPreviewInfo;
import space.space_spring.domain.event.application.port.out.LoadEventParticipantPort;
import space.space_spring.domain.event.application.port.out.LoadEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipants;
import space.space_spring.domain.event.domain.Events;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadEventService implements ReadEventUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadEventPort loadEventPort;
    private final LoadEventParticipantPort loadEventParticipantPort;

    @Override
    public List<ResultOfEventPreviewInfo> readEvents(Long spaceMemberId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);

        List<ResultOfEventPreviewInfo> resultOfEventPreviewInfos = new ArrayList<>();
        List<Event> events = loadEventPort.loadEvents(spaceMember.getSpaceId());
        for (Event event : events) {
            EventParticipants eventParticipants = loadEventParticipantPort.loadByEventId(event.getId());
            resultOfEventPreviewInfos.add(ResultOfEventPreviewInfo.builder()
                    .id(event.getId())
                    .date(event.getDate())
                    .image(event.getImage())
                    .name(event.getName())
                    .startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .totalNumberOfParticipants(eventParticipants.getTotalNumberOfParticipants())
                    .build());
        }

        return resultOfEventPreviewInfos;
    }

    @Override
    public Event readEvent(Long spaceMemberId, Long eventId) {
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(spaceMemberId);
        validateManager(spaceMember);
        return loadEventPort.loadEvent(eventId).orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
    }

    private void validateManager(SpaceMember spaceMember) {
        if (!spaceMember.isManager()) throw new CustomException(UNAUTHORIZED_USER);
    }

}
