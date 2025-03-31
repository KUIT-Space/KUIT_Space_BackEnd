package space.space_spring.domain.spaceMember.application.port.out;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

public interface UpdateSpaceMemberPort {
    SpaceMember update(SpaceMember spaceMember);
    SpaceMember updateManager(Long spaceMemberId,boolean isManger);
}
