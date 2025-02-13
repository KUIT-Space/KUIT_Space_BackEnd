package space.space_spring.domain.spaceMember.application.port.in;

import java.util.List;

public interface ValidateSpaceMemberUseCase {

    void validateSpaceMembersInSameSpace(List<Long> spaceMemberIds);
}
