package space.space_spring.domain.event.adapter.out.persistence.custom;

import static space.space_spring.domain.event.adapter.out.persistence.QEventParticipantJpaEntity.eventParticipantJpaEntity;
import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.enumStatus.BaseStatusType.INACTIVE;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;
import space.space_spring.domain.event.adapter.out.persistence.EventParticipantJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@RequiredArgsConstructor
public class EventParticipantRepositoryImpl implements EventParticipantRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<EventParticipantJpaEntity> findByEventAndStatusOrderByUpdatedAtDesc(EventJpaEntity event,
                                                                                    BaseStatusType status) {
        return jpaQueryFactory.selectFrom(eventParticipantJpaEntity)
                .where(eventParticipantJpaEntity.event.id.eq(event.getId())
                        .and(eventParticipantJpaEntity.status.eq(ACTIVE)))
                .orderBy(eventParticipantJpaEntity.lastModifiedAt.desc())
                .fetch();
    }

    @Override
    public void deleteAllByEvent(EventJpaEntity event) {
        jpaQueryFactory
                .update(eventParticipantJpaEntity)
                .set(eventParticipantJpaEntity.status, INACTIVE)
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
                        .and(eventParticipantJpaEntity.status.eq(ACTIVE)))
                .fetchFirst();

        return count != null;
    }

    @Override
    public void softDelete(EventParticipantJpaEntity eventParticipant) {
        jpaQueryFactory
                .update(eventParticipantJpaEntity)
                .set(eventParticipantJpaEntity.status, INACTIVE)
                .where(eventParticipantJpaEntity.id.eq(eventParticipant.getId()))
                .execute();
    }

    @Override
    public void updateActiveBySpaceMemberId(Long eventId, Long spaceMemberId) {
        jpaQueryFactory
                .update(eventParticipantJpaEntity)
                .set(eventParticipantJpaEntity.status, ACTIVE)
                .where(eventParticipantJpaEntity.event.id.eq(eventId)
                        .and(eventParticipantJpaEntity.spaceMember.id.eq(spaceMemberId)))
                .execute();
    }
}
