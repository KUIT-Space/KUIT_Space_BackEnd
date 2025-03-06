package space.space_spring.domain.event.adapter.in.web.updateEvent;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateEventParticipantRequest extends SelfValidating<UpdateEventParticipantRequest> {

    @NotEmpty(message = "수정하고자 하는 참여자는 한 명 이상어야 합니다.")
    private List<Long> spaceMemberId;
}
