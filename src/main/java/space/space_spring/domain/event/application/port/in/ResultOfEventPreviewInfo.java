package space.space_spring.domain.event.application.port.in;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.global.util.NaturalNumber;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResultOfEventPreviewInfo {

    private Long id;

    private String name;

    private String image;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private NaturalNumber totalNumberOfParticipants;
}
