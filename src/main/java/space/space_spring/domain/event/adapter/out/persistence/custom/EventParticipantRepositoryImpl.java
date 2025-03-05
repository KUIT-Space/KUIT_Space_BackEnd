package space.space_spring.domain.event.adapter.out.persistence.custom;

import static space.space_spring.domain.event.adapter.out.persistence.QEventParticipantJpaEntity.eventParticipantJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;
import space.space_spring.domain.event.adapter.out.persistence.EventParticipantJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@RequiredArgsConstructor
public class EventParticipantRepositoryImpl implements EventParticipantRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteAllByEvent(EventJpaEntity event) {
        jpaQueryFactory
                .update(eventParticipantJpaEntity)
                .set(eventParticipantJpaEntity.status, BaseStatusType.INACTIVE)
                .where(eventParticipantJpaEntity.event.eq(event))
                .execute();
    }

    @Override
    public boolean existsByEventAndSpaceMember(EventJpaEntity event, SpaceMemberJpaEntity spaceMember) {
        Integer count = jpaQueryFactory
                .selectOne()
                .from(eventParticipantJpaEntity)
                .where(eventParticipantJpaEntity.event.eq(event)
                        .and(eventParticipantJpaEntity.spaceMember.eq(spaceMember))
                        .and(eventParticipantJpaEntity.status.eq(BaseStatusType.ACTIVE)))
                .fetchFirst();

        return count != null;
    }

    @Override
    public void softDelete(EventParticipantJpaEntity eventParticipant) {
        jpaQueryFactory
                .update(eventParticipantJpaEntity)
                .set(eventParticipantJpaEntity.status, BaseStatusType.INACTIVE)
                .where(eventParticipantJpaEntity.id.eq(eventParticipant.getId()))
                .execute();
    }
}
