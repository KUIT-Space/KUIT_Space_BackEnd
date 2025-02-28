package space.space_spring.domain.post.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Board")
public class BoardJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    @NotNull
    private SpaceJpaEntity space;

    @Column(name = "discord_channel_id")
    @NotNull
    private Long discordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    @NotNull
    private BoardType boardType;

    @Column(name = "board_name")
    @NotNull
    private String boardName;

    @Column(name = "webhook_url")
    @NotNull
    private String webhookUrl;

    private BoardJpaEntity(SpaceJpaEntity space, Long discordId, BoardType boardType, String boardName, String webhookUrl) {
        this.space = space;
        this.discordId = discordId;
        this.boardName = boardName;
        this.boardType = boardType;
        this.webhookUrl = webhookUrl;
    }

    public static BoardJpaEntity create(SpaceJpaEntity space, Long discordId, BoardType boardType, String boardName, String webhookUrl) {
        return new BoardJpaEntity(space, discordId, boardType, boardName, webhookUrl);
    }
}
