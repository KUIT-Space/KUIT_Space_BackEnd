package space.space_spring.domain.post.application.port.in.createComment;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Content;

@Getter
public class CreateCommentCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;            // 댓글을 달 대상(= 게시글, 질문, Tip) 의 id 값

    private Long commentCreatorId;      // 댓글 작성자의 spaceMemberId

    private Content content;        // 댓글 내용

    private boolean isAnonymous;        // 익명 댓글 여부

    @Builder
    public CreateCommentCommand(Long spaceId, Long boardId, Long postId, Long commentCreatorId, String content, boolean isAnonymous) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.commentCreatorId = commentCreatorId;
        this.content = Content.of(content);
        this.isAnonymous = isAnonymous;
    }

    public Comment toDomainEntity(Long discordId) {
        return Comment.withoutId(boardId, discordId, postId, commentCreatorId, content, isAnonymous);
    }
}
