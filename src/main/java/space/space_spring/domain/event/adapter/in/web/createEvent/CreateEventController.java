package space.space_spring.domain.event.adapter.in.web.createEvent;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.CreateEventCommand;
import space.space_spring.domain.event.application.port.in.CreateEventUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.jwtLogin.JwtSpaceId;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Event", description = "행사 관련 API")
public class CreateEventController {

    private final CreateEventUseCase createEventUseCase;

    @Operation(summary = "행사 생성", description = """
        
        관리자가 새로운 행사를 생성합니다.
        
        """)
    @PostMapping("/event")
    public BaseResponse<CreateEventResponse> createEvent(@JwtLoginAuth Long spaceMemberId, @JwtSpaceId Long spaceId, @Validated @RequestBody CreateEventRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_EVENT_CREATE);
        }

        CreateEventCommand createEventCommand = CreateEventCommand.builder()
                .name(request.getName())
                .image(request.getImage())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        Long eventId = createEventUseCase.createEvent(spaceMemberId, createEventCommand);
        return new BaseResponse<>(new CreateEventResponse(eventId));
    }
}
