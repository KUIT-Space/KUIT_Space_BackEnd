package space.space_spring.domain.post.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Board")
public class BoardJpaEntity extends BaseEntity {

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

    @Column(name = "board_name")
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    @NotNull
    private BoardType boardType;

    @Column(name = "webhook_url")
    @NotNull
    private String webhookUrl;

    @Builder
    private BoardJpaEntity(SpaceJpaEntity space, Long discordId, String name, BoardType boardType, String webhookUrl) {
        this.space = space;
        this.discordId = discordId;
        this.name = name;
        this.boardType = boardType;
        this.webhookUrl = webhookUrl;
    }
}
