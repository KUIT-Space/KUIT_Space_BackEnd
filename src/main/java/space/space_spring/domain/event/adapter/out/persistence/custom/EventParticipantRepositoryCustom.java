package space.space_spring.domain.event.adapter.out.persistence.custom;

import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;

public interface EventParticipantRepositoryCustom {

    void deleteAllByEvent(EventJpaEntity event);

}
