package space.space_spring.domain.post.application.port.in.createComment;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.adapter.in.web.createComment.RequestOfUploadAttachment;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class CreateCommentCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;            // 댓글을 달 대상(= 게시글, 질문, Tip) 의 id 값

    private Long commentCreatorId;      // 댓글 작성자의 spaceMemberId

    private Content content;        // 댓글 내용

    private boolean isAnonymous;        // 익명 댓글 여부

    private List<UploadAttachmentCommand> attachmentCommands;

    @Builder
    public CreateCommentCommand(Long spaceId, Long boardId, Long postId, Long commentCreatorId, String content, boolean isAnonymous, List<RequestOfUploadAttachment> attachments) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.commentCreatorId = commentCreatorId;
        this.content = Content.of(content);
        this.isAnonymous = isAnonymous;
        this.attachmentCommands = mapToInputModel(attachments);
    }

    private static List<UploadAttachmentCommand> mapToInputModel(List<RequestOfUploadAttachment> attachments) {
        return attachments.stream()
                .map(attachment -> UploadAttachmentCommand.of(
                        attachment.getValueOfAttachmentType(),
                        attachment.getAttachment()))
                .toList();
    }

    public Comment toDomainEntity(Long discordId) {
        return Comment.withoutId(boardId, discordId, postId, commentCreatorId, content, isAnonymous);
    }
}
