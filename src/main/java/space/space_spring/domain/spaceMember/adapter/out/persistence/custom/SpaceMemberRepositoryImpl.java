package space.space_spring.domain.spaceMember.adapter.out.persistence.custom;

import static space.space_spring.domain.space.domain.QSpaceJpaEntity.spaceJpaEntity;
import static space.space_spring.domain.spaceMember.domian.QSpaceMemberJpaEntity.spaceMemberJpaEntity;
import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

@RequiredArgsConstructor
public class SpaceMemberRepositoryImpl implements SpaceMemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<SpaceMemberJpaEntity> findDefaultSpaceMember(Long userId, String defaultSpaceName) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(spaceMemberJpaEntity)
                .join(spaceMemberJpaEntity.space, spaceJpaEntity)
                .where(
                        spaceMemberJpaEntity.user.id.eq(userId),
                        spaceJpaEntity.name.eq(defaultSpaceName),
                        spaceMemberJpaEntity.status.eq(ACTIVE),
                        spaceJpaEntity.status.eq(ACTIVE)
                )
                .fetchOne());
    }
}
