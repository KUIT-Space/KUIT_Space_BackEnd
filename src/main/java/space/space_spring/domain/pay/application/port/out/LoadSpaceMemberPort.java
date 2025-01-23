package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;

public interface LoadSpaceMemberPort {

    SpaceMember loadSpaceMember(Long id);
}
