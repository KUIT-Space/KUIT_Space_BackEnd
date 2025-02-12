package space.space_spring.domain.spaceMember;

import java.util.List;

public interface ValidateSpaceMemberUseCase {

    void validateSpaceMembersInSameSpace(List<Long> spaceMemberIds);
}
