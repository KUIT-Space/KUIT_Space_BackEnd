package space.space_spring.domain.post.application.port.in.createComment;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.adapter.in.web.createComment.RequestOfCreateAttachment;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Content;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
public class CreateCommentCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;            // 댓글을 달 대상(= 게시글, 질문, Tip) 의 id 값

    private Long commentCreatorId;      // 댓글 작성자의 spaceMemberId

    private String content;        // 댓글 내용

    private Boolean isAnonymous;        // 익명 댓글 여부


    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    @Builder
    public CreateCommentCommand(Long spaceId,
                                Long boardId,
                                Long postId,
                                Long commentCreatorId,
                                String content,
                                Boolean isAnonymous,
                                OffsetDateTime createdAt,
                                OffsetDateTime lastModifiedAt) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.commentCreatorId = commentCreatorId;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.createdAt = createdAt == null ? null : createdAt.toLocalDateTime();
        this.lastModifiedAt = lastModifiedAt == null ? null : lastModifiedAt.toLocalDateTime();
    }


    /**
     * space 2.0 v1 에서는 댓글 수정 시에 첨부파일 update 요구사항 없음
     */

//    private static List<CreateAttachmentCommand> mapToInputModel(List<RequestOfCreateAttachment> attachments) {
//        return attachments.stream()
//                .map(attachment -> CreateAttachmentCommand.of(
//                        attachment.getValueOfAttachmentType(),
//                        attachment.getAttachment()))
//                .toList();
//    }

    public Comment toDomainEntity(Long discordId) {
        return Comment.withoutId(boardId, discordId, postId, commentCreatorId, content, isAnonymous);
    }
}
