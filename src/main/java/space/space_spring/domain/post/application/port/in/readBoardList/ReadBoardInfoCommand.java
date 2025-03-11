package space.space_spring.domain.post.application.port.in.readBoardList;

import lombok.Getter;

@Getter
public class ReadBoardInfoCommand {

    private Long boardId;

    private String boardName;

    private Long tagId;

    private String tagName;

    private boolean isSubscribed;

    private ReadBoardInfoCommand(Long boardId, String boardName, Long tagId, String tagName, boolean isSubscribed) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.tagId = tagId;
        this.tagName = tagName;
        this.isSubscribed = isSubscribed;
    }

    public static ReadBoardInfoCommand of(Long boardId, String boardName, Long tagId, String tagName, boolean isSubscribed) {
        return new ReadBoardInfoCommand(boardId, boardName, tagId, tagName, isSubscribed);
    }
}
