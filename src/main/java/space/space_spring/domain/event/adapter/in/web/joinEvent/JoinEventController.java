package space.space_spring.domain.event.adapter.in.web.joinEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.JoinEventUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
public class JoinEventController {

    private final JoinEventUseCase joinEventUseCase;

    @PostMapping("/event/{eventId}/join")
    public BaseResponse<SuccessResponse> joinEvent(@JwtLoginAuth Long id, @PathVariable Long eventId) {
        boolean isJoinSuccess = joinEventUseCase.joinEvent(id, eventId);
        return new BaseResponse<>(new SuccessResponse(isJoinSuccess));
    }
}
