package space.space_spring.domain.event.adapter.in.web.readEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.ReadEventParticipantUseCase;
import space.space_spring.domain.event.application.port.in.ReadEventUseCase;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipantInfos;
import space.space_spring.domain.event.domain.Events;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class ReadEventController {

    private final ReadEventUseCase readEventUseCase;
    private final ReadEventParticipantUseCase readEventParticipantUseCase;

    @GetMapping("/events")
    public BaseResponse<ReadEventsResponse> readEvents(@JwtLoginAuth Long id) {
        Events events = readEventUseCase.readEvents(id);
        return new BaseResponse<>(ReadEventsResponse.create(events));
    }

    @GetMapping("/event/{eventId}")
    public BaseResponse<ReadEventInfoResponse> readEvent(@JwtLoginAuth Long id, @PathVariable Long eventId) {
        Event event = readEventUseCase.readEvent(eventId);
        EventParticipantInfos eventParticipantInfos = readEventParticipantUseCase.readEventParticipants(eventId);
        return new BaseResponse<>(ReadEventInfoResponse.create(event, eventParticipantInfos));
    }
}
