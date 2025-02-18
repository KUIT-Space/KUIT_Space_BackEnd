package space.space_spring.domain.event.adapter.in.web.createEvent;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.CreateEventCommand;
import space.space_spring.domain.event.application.port.in.CreateEventUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

@RestController
@RequiredArgsConstructor
public class CreateEventController {

    private final CreateEventUseCase createEventUseCase;

    @PostMapping("/event")
    public BaseResponse<CreateEventResponse> createEvent(@JwtLoginAuth Long id, @Validated @RequestBody CreateEventRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_EVENT_CREATE);
        }

        CreateEventCommand createEventCommand = CreateEventCommand.builder()
                .name(request.getName())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        Long eventId = createEventUseCase.createEvent(id, createEventCommand);
        return new BaseResponse<>(new CreateEventResponse(eventId));
    }
}
