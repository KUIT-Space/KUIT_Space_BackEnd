package space.space_spring.domain.spaceMember;

import java.util.List;

public interface LoadSpaceMemberPort {

    SpaceMember loadById(Long id);

    List<SpaceMember> loadAllById(List<Long> ids);
}
