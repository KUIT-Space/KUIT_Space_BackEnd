package space.space_spring.domain.spaceMember.application.port.out;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

import java.util.List;

public interface CreateSpaceMemberPort {
    SpaceMember createSpaceMember(SpaceMember spaceMember);
    List<SpaceMember> createSpaceMembers(List<SpaceMember> spaceMembers);
}
