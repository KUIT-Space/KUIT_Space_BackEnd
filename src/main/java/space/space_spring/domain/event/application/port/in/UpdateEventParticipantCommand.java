package space.space_spring.domain.event.application.port.in;

import java.util.Collections;
import java.util.List;
import lombok.Builder;

public class UpdateEventParticipantCommand {

    private List<Long> spaceMemberIds;

    @Builder
    public UpdateEventParticipantCommand(List<Long> spaceMemberIds) {
        this.spaceMemberIds = spaceMemberIds;
    }

    public List<Long> getSpaceMemberIds() {
        return Collections.unmodifiableList(this.spaceMemberIds);
    }
}
