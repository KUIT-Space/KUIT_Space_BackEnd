package space.space_spring.domain.spaceMember.application.port.in;

import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

public interface CreateSpaceMemberUseCase {
    SpaceMember create(SpaceMember spaceMember);
    SpaceMembers create(SpaceMembers spaceMembers);
}
