package space.space_spring.domain.spaceMember;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

import java.util.List;

public interface LoadSpaceMemberPort {

    SpaceMember loadSpaceMemberById(Long id);
}
