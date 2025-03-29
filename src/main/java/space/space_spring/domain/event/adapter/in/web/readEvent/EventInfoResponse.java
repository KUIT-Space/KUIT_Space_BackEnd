package space.space_spring.domain.event.adapter.in.web.readEvent;

import static space.space_spring.global.util.TimeConverter.convertUtcToKst;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.event.application.port.in.ResultOfEventPreviewInfo;

@Getter
@AllArgsConstructor
public class EventInfoResponse {

    private Long id;

    private String name;

    private String image;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int totalNumberOfParticipants;

    public static EventInfoResponse create(ResultOfEventPreviewInfo result) {
        return new EventInfoResponse(
                result.getId(),
                result.getName(),
                result.getImage(),
                convertUtcToKst(result.getDate()),
                convertUtcToKst(result.getStartTime()),
                convertUtcToKst(result.getEndTime()),
                result.getTotalNumberOfParticipants().getNumber());
    }
}
