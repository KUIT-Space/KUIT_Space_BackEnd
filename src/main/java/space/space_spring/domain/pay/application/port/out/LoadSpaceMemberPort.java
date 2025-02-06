package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

public interface LoadSpaceMemberPort {

    SpaceMember loadSpaceMember(Long id);
}
