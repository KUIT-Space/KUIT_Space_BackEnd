package space.space_spring.domain.spaceMember.application.port.in;

import space.space_spring.domain.spaceMember.application.port.out.query.SpaceMemberDetail;

import java.util.List;

public interface ReadSpaceMemberUseCase {

    List<SpaceMemberDetail> readAllSpaceMembers(Long spaceId);
}
