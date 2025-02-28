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

    private Long postCreatorId;

    private Long boardId;

    private String title;

    private Content content;

    private List<AttachmentOfCreateCommand> attachments;

    @Builder
    public CreatePostCommand(Long postCreatorId, Long boardId, String title, String content, List<AttachmentOfCreate> attachments) {
        this.postCreatorId = postCreatorId;
        this.boardId = boardId;
        this.title = title;
        this.content = Content.of(content);
        this.attachments = mapToInputModel(attachments);
    }

    private static List<AttachmentOfCreateCommand> mapToInputModel(List<AttachmentOfCreate> attachments) {
        List<AttachmentOfCreateCommand> result = new ArrayList<>();
        for (AttachmentOfCreate attachment : attachments) {
            result.add(AttachmentOfCreateCommand.create(attachment.getValueOfAttachmentType(), attachment.getAttachmentUrl()));
        }
        return result;
    }

    public Post toPostDomainEntity(Long discordMessageId) {
        return Post.withoutId(discordMessageId, boardId, postCreatorId, title, content);
    }
}
