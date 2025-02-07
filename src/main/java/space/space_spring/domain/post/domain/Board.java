package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.domain.space.domain.Space;

@Getter
public class Board {

    private Long id;

    private Space space;

    private Long discordId;

    private String name;

    private BoardType boardType;

    private String webhookUrl;

    private Board(Long id, Space space, Long discordId, String name, BoardType boardType, String webhookUrl) {
        this.id = id;
        this.space = space;
        this.discordId = discordId;
        this.name = name;
        this.boardType = boardType;
        this.webhookUrl = webhookUrl;
    }

    // 정적 팩토리 메서드
    public static Board of(Long id, Space space, Long discordId, String name, BoardType boardType, String webhookUrl) {
        return new Board(id, space, discordId, name, boardType, webhookUrl);
    }
}
