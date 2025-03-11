package space.space_spring.domain.post.application.port.in.updateComment;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.adapter.in.web.updateComment.RequestOfUpdateAttachment;

import java.util.List;

@Getter
public class UpdateCommentCommand {

    private Long commentId;

    private Long spaceId;

    private Long boardId;

    private Long postId;            // 댓글을 달 대상(= 게시글, 질문, Tip) 의 id 값

    private Long commentCreatorId;      // 댓글 작성자의 spaceMemberId

    private String content;        // 댓글 내용

    private boolean isAnonymous;        // 익명 댓글 여부

    @Builder
    public UpdateCommentCommand(Long commentId, Long spaceId, Long boardId, Long postId, Long commentCreatorId, String content, boolean isAnonymous) {
        this.commentId = commentId;
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.commentCreatorId = commentCreatorId;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }


    /**
     * space 2.0 v1 에서는 댓글 수정 시에 첨부파일 update 요구사항 없음
     */

    private static List<UpdateAttachmentCommand> mapToUpdateAttachmentCommand(List<RequestOfUpdateAttachment> attachments) {
        return attachments.stream()
                .map(attachment -> UpdateAttachmentCommand.of(
                        attachment.getValueOfAttachmentType(),
                        attachment.getAttachment()))
                .toList();
    }
}
