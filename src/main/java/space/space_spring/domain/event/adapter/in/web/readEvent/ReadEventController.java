package space.space_spring.domain.event.adapter.in.web.readEvent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.ReadEventParticipantUseCase;
import space.space_spring.domain.event.application.port.in.ReadEventUseCase;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.event.domain.EventParticipantInfos;
import space.space_spring.domain.event.domain.Events;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.jwtLogin.JwtSpaceId;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Event", description = "행사 관련 API")
public class ReadEventController {

    private final ReadEventUseCase readEventUseCase;
    private final ReadEventParticipantUseCase readEventParticipantUseCase;

    @Operation(summary = "행사 전체 조회", description = """
        
        관리자가 해당 스페이스에서 생성된 모든 행사를 조회합니다.
        
        """)
    @GetMapping("/events")
    public BaseResponse<ReadEventsResponse> readEvents(@JwtLoginAuth Long spaceMemberId, @JwtSpaceId Long spaceId) {
        Events events = readEventUseCase.readEvents(spaceMemberId);
        return new BaseResponse<>(ReadEventsResponse.create(events));
    }

    @Operation(summary = "행사 단건 조회", description = """
        
        관리자가 특정 행사에 대한 세부 정보를 조회합니다.
        
        """)
    @GetMapping("/event/{eventId}")
    public BaseResponse<ReadEventInfoResponse> readEvent(@JwtLoginAuth Long spaceMemberId, @JwtSpaceId Long spaceId, @PathVariable Long eventId) {
        Event event = readEventUseCase.readEvent(spaceMemberId, eventId);
        EventParticipantInfos eventParticipantInfos = readEventParticipantUseCase.readEventParticipants(spaceMemberId, eventId);
        return new BaseResponse<>(ReadEventInfoResponse.create(event, eventParticipantInfos));
    }
}
