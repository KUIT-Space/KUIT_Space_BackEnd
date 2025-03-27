package space.space_spring.domain.event.adapter.out.persistence.custom;

import java.util.List;
import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;
import space.space_spring.domain.event.adapter.out.persistence.EventParticipantJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface EventParticipantRepositoryCustom {

    List<EventParticipantJpaEntity> findByEventAndStatusOrderByUpdatedAtDesc(EventJpaEntity event, BaseStatusType status);

    void deleteAllByEvent(EventJpaEntity event);

    boolean existsByEventAndSpaceMember(EventJpaEntity event, SpaceMemberJpaEntity spaceMember);

    void softDelete(EventParticipantJpaEntity eventParticipant);
}
