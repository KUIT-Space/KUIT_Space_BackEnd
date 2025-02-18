package space.space_spring.domain.event.adapter.in.web;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

@NoArgsConstructor
@Getter
public class CreateEventRequest extends SelfValidating<CreateEventRequest> {

    @NotBlank(message = "행사 이름은 공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "행사 날짜는 공백일 수 없습니다.")
    private LocalDateTime date;

    @NotBlank(message = "행사 시작 시간은 공백일 수 없습니다.")
    private LocalDateTime startTime;

    @NotBlank(message = "행사 종료 시간은 공백일 수 없습니다.")
    private LocalDateTime endTime;

    public CreateEventRequest(String name, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.validateSelf();
    }

    @AssertTrue(message = "행사 시작 시간은 종료 시간 이후일 수 없습니다.")
    private boolean isStartTimeValid() {
        return startTime != null && endTime != null && !startTime.isAfter(endTime);
    }
}
