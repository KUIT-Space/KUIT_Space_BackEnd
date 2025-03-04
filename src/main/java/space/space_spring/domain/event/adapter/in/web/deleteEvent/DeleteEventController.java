package space.space_spring.domain.event.adapter.in.web.deleteEvent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.DeleteEventUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.jwtLogin.JwtSpaceId;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Event", description = "행사 관련 API")
public class DeleteEventController {

    private final DeleteEventUseCase deleteEventUseCase;

    @Operation(summary = "행사 삭제", description = """
        
        관리자가 행사를 삭제합니다.
        
        """)
    @DeleteMapping("/event/{eventId}")
    public BaseResponse<SuccessResponse> DeleteEvent(@JwtLoginAuth Long spaceMemberId, @JwtSpaceId Long spaceId, @PathVariable Long eventId) {
        return new BaseResponse<>(new SuccessResponse(deleteEventUseCase.deleteEvent(spaceMemberId, eventId)));
    }
}
