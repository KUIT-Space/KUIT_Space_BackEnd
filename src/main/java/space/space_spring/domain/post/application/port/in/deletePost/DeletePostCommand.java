package space.space_spring.domain.post.application.port.in.deletePost;

import lombok.Getter;

@Getter
public class DeletePostCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;

    private Long postCreatorId;

    private DeletePostCommand(Long spaceId, Long boardId, Long postId, Long postCreatorId) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.postCreatorId = postCreatorId;
    }

    public static DeletePostCommand of(Long spaceId, Long boardId, Long postId, Long postCreatorId) {
        return new DeletePostCommand(spaceId, boardId, postId, postCreatorId);
    }
}
