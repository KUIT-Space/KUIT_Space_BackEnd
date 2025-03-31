package space.space_spring.domain.post.adapter.out.persistence.postBase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Post_Base")
public class PostBaseJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_base_id")
    @NotNull
    private Long id;

    @Column(name = "discord_message_id")
    @NotNull
    private Long discordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @NotNull
    private BoardJpaEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_member_id")
    @NotNull
    private SpaceMemberJpaEntity spaceMember;

    @Lob
    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Builder
    private PostBaseJpaEntity(Long discordId, BoardJpaEntity board, SpaceMemberJpaEntity spaceMember, String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        super(createdAt, lastModifiedAt, baseStatus);
        this.discordId = discordId;
        this.board = board;
        this.spaceMember = spaceMember;
        this.content = content;
    }

    public static PostBaseJpaEntity create(Long discordId, BoardJpaEntity board, SpaceMemberJpaEntity spaceMember, String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt, BaseStatusType baseStatus) {
        return new PostBaseJpaEntity(discordId, board, spaceMember, content, createdAt, lastModifiedAt, baseStatus);
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
