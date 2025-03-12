package space.space_spring.domain.event.adapter.in.web.joinEvent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.JoinEventUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Event", description = "행사 관련 API")
public class JoinEventController {

    private final JoinEventUseCase joinEventUseCase;

    @Operation(summary = "행사 참여", description = """
        
        행사 id로 해당 행사에 참여합니다.
        
        """)
    @PostMapping("/event/{eventId}/join")
    public BaseResponse<SuccessResponse> joinEvent(@JwtLoginAuth Long spaceMemberId, @PathVariable Long spaceId, @PathVariable Long eventId) {
        boolean isJoinSuccess = joinEventUseCase.joinEvent(spaceMemberId, eventId);
        return new BaseResponse<>(new SuccessResponse(isJoinSuccess));
    }
}
