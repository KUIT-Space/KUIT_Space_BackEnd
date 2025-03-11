package space.space_spring.domain.post.adapter.out.persistence.subscription;

import jakarta.annotation.Nullable;
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
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Subscription")
public class SubscriptionJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue
    @Column(name = "subscription_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity spaceMember;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @NotNull
    private BoardJpaEntity board;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @Nullable
    private TagJpaEntity tag;

    @Builder
    private SubscriptionJpaEntity(SpaceMemberJpaEntity spaceMember, BoardJpaEntity board, TagJpaEntity tag, LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        super(createdAt, lastModifiedAt, baseStatus);
        this.spaceMember = spaceMember;
        this.board = board;
        this.tag = tag;
    }

    public static SubscriptionJpaEntity create(SpaceMemberJpaEntity spaceMember, BoardJpaEntity board, TagJpaEntity tag, LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        return SubscriptionJpaEntity.builder()
                .spaceMember(spaceMember)
                .board(board)
                .tag(tag)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .baseStatus(baseStatus)
                .build();
    }

}
