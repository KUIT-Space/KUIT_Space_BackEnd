package space.space_spring.domain.event.adapter.out.persistence.custom;

import static space.space_spring.domain.event.adapter.out.persistence.QEventJpaEntity.eventJpaEntity;
import static space.space_spring.global.common.enumStatus.BaseStatusType.INACTIVE;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;

@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void softDelete(EventJpaEntity event) {
        jpaQueryFactory
                .update(eventJpaEntity)
                .set(eventJpaEntity.status, INACTIVE)
                .where(eventJpaEntity.id.eq(event.getId()))
                .execute();
    }
}
