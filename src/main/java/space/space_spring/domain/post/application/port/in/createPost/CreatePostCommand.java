package space.space_spring.domain.post.application.port.in.createPost;

import lombok.Getter;
import space.space_spring.domain.post.adapter.in.web.createPost.AttachmentOfCreate;
import space.space_spring.domain.post.domain.Content;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreatePostCommand {

    private Long postCreatorId;

    private Long boardId;

    private String title;

    private Content content;

    private List<AttachmentOfCreateCommand> attachments;

    private CreatePostCommand(Long postCreatorId, Long boardId, String title, Content content, List<AttachmentOfCreateCommand> attachments) {
        this.postCreatorId = postCreatorId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
    }

    public static CreatePostCommand create(Long postCreatorId, Long boardId, String title, String content, List<AttachmentOfCreate> attachments) {
        return new CreatePostCommand(postCreatorId, boardId, title, Content.of(content), mapToInputModel(attachments));
    }

    private static List<AttachmentOfCreateCommand> mapToInputModel(List<AttachmentOfCreate> attachments) {
        List<AttachmentOfCreateCommand> result = new ArrayList<>();
        for (AttachmentOfCreate attachment : attachments) {
            result.add(AttachmentOfCreateCommand.create(attachment.getValueOfAttachmentType(), attachment.getAttachmentUrl()));
        }
        return result;
    }
}
