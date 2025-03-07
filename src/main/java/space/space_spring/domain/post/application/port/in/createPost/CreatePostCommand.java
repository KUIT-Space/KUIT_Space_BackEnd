package space.space_spring.domain.post.application.port.in.createPost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.adapter.in.web.createPost.AttachmentOfCreate;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreatePostCommand {

    private Long spaceId;

    private Long boardId;

    private Long postCreatorId;

    private String title;

    private Content content;

    private List<AttachmentOfCreateCommand> attachments;

    private Boolean isAnonymous;

    @Builder
    public CreatePostCommand(Long spaceId, Long boardId, Long postCreatorId, String title, String content, List<AttachmentOfCreate> attachments, Boolean isAnonymous) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postCreatorId = postCreatorId;
        this.title = title;
        this.content = Content.of(content);
        this.attachments = mapToInputModel(attachments);
        this.isAnonymous = isAnonymous;
    }

    private static List<AttachmentOfCreateCommand> mapToInputModel(List<AttachmentOfCreate> attachments) {
        List<AttachmentOfCreateCommand> result = new ArrayList<>();
        for (AttachmentOfCreate attachment : attachments) {
            result.add(AttachmentOfCreateCommand.create(attachment.getValueOfAttachmentType(), attachment.getAttachmentUrl()));
        }
        return result;
    }

    public Post toPostDomainEntity(Long discordMessageId) {
        return Post.withoutId(discordMessageId, boardId, postCreatorId, title, content, isAnonymous);
    }
}
