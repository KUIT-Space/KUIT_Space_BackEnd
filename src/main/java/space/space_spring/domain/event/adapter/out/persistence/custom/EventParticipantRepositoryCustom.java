package space.space_spring.domain.event.adapter.out.persistence.custom;

import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;
import space.space_spring.domain.event.adapter.out.persistence.EventParticipantJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

public interface EventParticipantRepositoryCustom {

    void deleteAllByEvent(EventJpaEntity event);

    boolean existsByEventAndSpaceMember(EventJpaEntity event, SpaceMemberJpaEntity spaceMember);

    void softDelete(EventParticipantJpaEntity eventParticipant);
}
