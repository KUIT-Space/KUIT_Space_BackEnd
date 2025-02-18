package space.space_spring.domain.event.adapter.out.persistence;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.event.application.port.out.CreateEventPort;
import space.space_spring.domain.event.domain.Event;
import space.space_spring.domain.space.adapter.out.persistence.SpringDataSpace;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.exception.CustomException;

@RequiredArgsConstructor
@Repository
public class EventPersistenceAdapter implements CreateEventPort {

    private final EventRepository eventRepository;
    private final SpringDataSpace spaceRepository;
    private final EventMapper eventMapper;

    @Override
    public Event createEvent(Event event) {
        SpaceJpaEntity spaceJpaEntity = spaceRepository.findById(event.getSpaceId()).orElseThrow(
                () -> new CustomException(SPACE_NOT_FOUND));

        EventJpaEntity eventJpaEntity = eventRepository.save(eventMapper.toJpaEntity(spaceJpaEntity, event));

        return eventMapper.toDomainEntity(eventJpaEntity);
    }
}
