package space.space_spring.domain.spaceMember.application.port.out.query;

import java.util.List;

public interface SpaceMemberQueryPort {

    List<SpaceMemberDetail> loadSpaceMemberDetails(Long spaceId);
}
