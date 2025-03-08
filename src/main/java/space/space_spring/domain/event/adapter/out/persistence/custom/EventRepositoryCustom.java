package space.space_spring.domain.event.adapter.out.persistence.custom;

import space.space_spring.domain.event.adapter.out.persistence.EventJpaEntity;

public interface EventRepositoryCustom {

    void softDelete(EventJpaEntity event);

}
