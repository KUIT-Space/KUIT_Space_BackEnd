package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Board {

    private Long id;

    private Long spaceId;

    private Long discordId;

    private String boardName;

    private BoardType boardType;

    private String webhookUrl;

    private Board(Long id, Long spaceId, Long discordId, String boardName, BoardType boardType, String webhookUrl) {
        this.id = id;
        this.spaceId = spaceId;
        this.discordId = discordId;
        this.boardName = boardName;
        this.boardType = boardType;
        this.webhookUrl = webhookUrl;
    }

    // 정적 팩토리 메서드
    public static Board of(Long id, Long spaceId, Long discordId, String boardName, BoardType boardType, String webhookUrl) {
        return new Board(id, spaceId, discordId, boardName, boardType, webhookUrl);
    }
}
