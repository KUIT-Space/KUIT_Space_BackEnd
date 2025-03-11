package space.space_spring.domain.post.application.port.in.deleteComment;

import lombok.Getter;

@Getter
public class DeleteCommentCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;

    private Long commentId;

    private Long commentCreatorId;

    private DeleteCommentCommand(Long spaceId, Long boardId, Long postId, Long commentId, Long commentCreatorId) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.commentId = commentId;
        this.commentCreatorId = commentCreatorId;
    }

    public static DeleteCommentCommand of(Long spaceId, Long boardId, Long postId, Long commentId, Long commentCreatorId) {
        return new DeleteCommentCommand(spaceId, boardId, postId, commentId, commentCreatorId);
    }
}
