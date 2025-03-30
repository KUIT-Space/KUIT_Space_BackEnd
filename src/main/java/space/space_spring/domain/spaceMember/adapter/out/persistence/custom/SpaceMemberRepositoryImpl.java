package space.space_spring.domain.spaceMember.adapter.out.persistence.custom;

import static space.space_spring.domain.space.domain.QSpaceJpaEntity.spaceJpaEntity;
import static space.space_spring.domain.spaceMember.domian.QSpaceMemberJpaEntity.spaceMemberJpaEntity;
import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Override
    public List<SpaceMemberJpaEntity> findAllByIdInOrder(List<Long> ids) {
        List<SpaceMemberJpaEntity> members = jpaQueryFactory.selectFrom(spaceMemberJpaEntity)
                .where(spaceMemberJpaEntity.id.in(ids))
                .fetch();

        // ID 순서에 맞게 정렬
        return ids.stream()
                .map(id -> members.stream().filter(member -> member.getId().equals(id)).findFirst().orElse(null))
                .filter(member -> member != null && member.isActive())
                .collect(Collectors.toList());
    }
}
