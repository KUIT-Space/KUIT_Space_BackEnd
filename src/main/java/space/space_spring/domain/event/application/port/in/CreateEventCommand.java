package space.space_spring.domain.event.application.port.in;

import java.time.LocalDateTime;
import lombok.Builder;
import space.space_spring.domain.event.domain.Event;

public class CreateEventCommand {

    private String name;

    private String image;

    private LocalDateTime date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    public CreateEventCommand(String name, String image, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.image = image;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event toDomainEntity(Long spaceId) {
        return Event.builder()
                .spaceId(spaceId)
                .name(this.name)
                .image(this.image)
                .date(this.date)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }
}
