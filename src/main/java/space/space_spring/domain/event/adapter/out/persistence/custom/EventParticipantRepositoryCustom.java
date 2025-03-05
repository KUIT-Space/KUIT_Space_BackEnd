package space.space_spring.domain.event.adapter.out.persistence.custom;

import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;

public interface EventParticipantRepositoryCustom {

    void deleteAllByEvent(EventJpaEntity event);

    void deleteByEventAndSpaceMember(EventJpaEntity event, SpaceMemberJpaEntity spaceMember);

    boolean existsByEventAndSpaceMember(EventJpaEntity event, SpaceMemberJpaEntity spaceMember);
}
