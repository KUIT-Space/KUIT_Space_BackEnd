package space.space_spring.domain.spaceMember.adapter.out.persistence.custom;

import java.util.Optional;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

public interface SpaceMemberRepositoryCustom {
    Optional<SpaceMemberJpaEntity> findDefaultSpaceMember(Long userId, String defaultSpaceName);
}
