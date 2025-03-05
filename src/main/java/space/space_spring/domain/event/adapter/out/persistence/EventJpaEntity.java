package space.space_spring.domain.event.adapter.out.persistence;

import static space.space_spring.global.common.enumStatus.BaseStatusType.INACTIVE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Event")
public class EventJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue
    @Column(name = "event_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    @NotNull
    private SpaceJpaEntity space;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "image")
    @NotNull
    private String image;

    @Column(name = "date")
    @NotNull
    private LocalDateTime date;

    @Column(name = "start_time")
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @NotNull
    private LocalDateTime endTime;

    @Builder
    private EventJpaEntity(SpaceJpaEntity space, String name, String image, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        this.space = space;
        this.name = name;
        this.image = image;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static EventJpaEntity create(SpaceJpaEntity space, String name, String image, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return EventJpaEntity.builder()
                .space(space)
                .name(name)
                .image(image)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public boolean isNotActive() {
        return this.getStatus().equals(INACTIVE);
    }
}
