package space.space_spring.domain.spaceMember.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.spaceMember.application.port.out.query.SpaceMemberDetail;
import space.space_spring.domain.spaceMember.application.port.out.query.SpaceMemberQueryPort;
import space.space_spring.domain.spaceMember.domian.QSpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpaceMemberQueryAdapter implements SpaceMemberQueryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SpaceMemberDetail> loadSpaceMemberDetails(Long spaceId) {
        QSpaceMemberJpaEntity spaceMember = QSpaceMemberJpaEntity.spaceMemberJpaEntity;
        return queryFactory.select(
                        Projections.constructor(SpaceMemberDetail.class,
                                spaceMember.id,
                                spaceMember.nickname,
                                spaceMember.profileImageUrl,
                                spaceMember.isManager))
                .from(spaceMember)
                .where(spaceMember.space.id.eq(spaceId)
                        .and(spaceMember.status.eq(BaseStatusType.ACTIVE)))
                .fetch();
    }
}
