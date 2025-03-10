package space.space_spring.domain.post.adapter.out.persistence.subscription;

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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Subscription")
public class SubscriptionJpaEntity {
    @Id
    @GeneratedValue
    @Column(name = "subscription_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity spaceMember;

    @Column(name = "board_id")
    @NotNull
    private Long boardId;

    @Column(name = "tag_id")
    @NotNull
    private Long tagId;

    @Builder
    private SubscriptionJpaEntity(SpaceMemberJpaEntity spaceMember, Long boardId, Long tagId) {
        this.spaceMember = spaceMember;
        this.boardId = boardId;
        this.tagId = tagId;
    }

    public static SubscriptionJpaEntity create(SpaceMemberJpaEntity spaceMember, Long boardId, Long tagId) {
        return SubscriptionJpaEntity.builder()
                .spaceMember(spaceMember)
                .boardId(boardId)
                .tagId(tagId)
                .build();
    }

}
