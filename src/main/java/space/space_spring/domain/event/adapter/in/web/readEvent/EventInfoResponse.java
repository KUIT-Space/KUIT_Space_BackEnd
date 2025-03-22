package space.space_spring.domain.event.adapter.in.web.readEvent;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import space.space_spring.domain.event.application.port.in.ResultOfEventPreviewInfo;
import space.space_spring.domain.event.domain.Event;

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
                result.getDate(),
                result.getStartTime(),
                result.getEndTime(),
                result.getTotalNumberOfParticipants().getNumber());
    }
}
