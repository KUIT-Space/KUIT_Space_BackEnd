package space.space_spring.domain.event.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Event_Participant")
public class EventParticipantJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue
    @Column(name = "event_participant_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @NotNull
    private EventJpaEntity event;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity spaceMember;

    @Builder
    private EventParticipantJpaEntity(EventJpaEntity event, SpaceMemberJpaEntity spaceMember) {
        this.event = event;
        this.spaceMember = spaceMember;
    }

    static EventParticipantJpaEntity create(EventJpaEntity event, SpaceMemberJpaEntity spaceMember) {
        return EventParticipantJpaEntity.builder()
                .event(event)
                .spaceMember(spaceMember)
                .build();
    }

}
