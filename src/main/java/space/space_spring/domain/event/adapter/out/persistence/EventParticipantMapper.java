package space.space_spring.domain.event.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.event.domain.EventParticipant;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseInfo;

@Component
@RequiredArgsConstructor
public class EventParticipantMapper {

    public EventParticipant toDomainEntity(EventParticipantJpaEntity eventParticipantJpaEntity) {
        BaseInfo baseInfo = BaseInfo.of(eventParticipantJpaEntity.getCreatedAt(), eventParticipantJpaEntity.getLastModifiedAt(), eventParticipantJpaEntity.getStatus());

        return EventParticipant.create(
                eventParticipantJpaEntity.getId(),
                eventParticipantJpaEntity.getEvent().getId(),
                eventParticipantJpaEntity.getSpaceMember().getId(),
                baseInfo
        );
    }

    public EventParticipantJpaEntity toJpaEntity(EventJpaEntity eventJpaEntity, SpaceMemberJpaEntity spaceMemberJpaEntity) {
        return EventParticipantJpaEntity.create(
                eventJpaEntity,
                spaceMemberJpaEntity
        );
    }
}
