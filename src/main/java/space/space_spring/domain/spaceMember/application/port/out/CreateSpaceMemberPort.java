package space.space_spring.domain.spaceMember.application.port.out;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

public interface CreateSpaceMemberPort {
    SpaceMember createSpaceMember(SpaceMember spaceMember);
}
