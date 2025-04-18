package space.space_spring.domain.event.adapter.in.web.createEvent;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.event.application.port.in.CreateEventCommand;
import space.space_spring.domain.event.application.port.in.CreateEventUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.S3Uploader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Event", description = "행사 관련 API")
public class CreateEventController {

    private final CreateEventUseCase createEventUseCase;
    private final S3Uploader s3Uploader;

    @Operation(summary = "행사 생성", description = """
        
        관리자가 새로운 행사를 생성합니다.
        
        """)
    @PostMapping(value = "/event", consumes = "multipart/form-data")
    public BaseResponse<CreateEventResponse> createEvent(@JwtLoginAuth Long spaceMemberId, @PathVariable Long spaceId, @Validated @ModelAttribute CreateEventRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_EVENT_CREATE);
        }

        String eventDirName = "eventImg";
        String eventImgUrl = null;
        try {
            eventImgUrl = s3Uploader.upload(request.getImage(), eventDirName);
        } catch (IOException e) {
            throw new CustomException(MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY);
        }

        LocalDateTime parsedEventDate = parseDate(request.getDate());
        LocalDateTime parsedStartTime = parseDate(request.getStartTime());
        LocalDateTime parsedEndTime = parseDate(request.getEndTime());
        validateDateRange(parsedEventDate, parsedStartTime, parsedEndTime);

        CreateEventCommand createEventCommand = CreateEventCommand.builder()
                .name(request.getName())
                .image(eventImgUrl)
                .date(parsedEventDate)
                .startTime(parsedStartTime)
                .endTime(parsedEndTime)
                .build();

        Long eventId = createEventUseCase.createEvent(spaceMemberId, createEventCommand);
        return new BaseResponse<>(new CreateEventResponse(eventId));
    }

    private void validateDateRange(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new CustomException(INVALID_EVENT_TIME_RANGE);
        }

        if (!date.toLocalDate().equals(startTime.toLocalDate())) {
            throw new CustomException(INVALID_EVENT_DATE);
        }
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr)
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .withZoneSameInstant(ZoneId.of("UTC"))
                    .toLocalDateTime();

        } catch (DateTimeParseException e) {
            throw new CustomException(INVALID_DATETIME_TYPE);
        }
    }
}