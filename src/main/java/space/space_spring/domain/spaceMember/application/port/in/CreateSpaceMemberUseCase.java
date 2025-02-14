package space.space_spring.domain.spaceMember.application.port.in;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

public interface CreateSpaceMemberUseCase {
    SpaceMember create(SpaceMember spaceMember);
}
