package space.space_spring.domain.post.adapter.in.web.readBoardList;

import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardInfoCommand;

@Getter
public class ResponseOfBoardInfo {

    private Long boardId;

    private String boardName;

    private String tagName;

    private Boolean isSubscribed;

    private ResponseOfBoardInfo(Long boardId, String boardName, String tagName, boolean isSubscribed) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.tagName = tagName;
        this.isSubscribed = isSubscribed;
    }

    public static ResponseOfBoardInfo of(ReadBoardInfoCommand boardInfo) {
        return new ResponseOfBoardInfo(
                boardInfo.getBoardId(),
                boardInfo.getBoardName(),
                boardInfo.getTagName(),
                boardInfo.isSubscribed()
        );
    }
}
