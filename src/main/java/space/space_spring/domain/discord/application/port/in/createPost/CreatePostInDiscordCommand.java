package space.space_spring.domain.discord.application.port.in.createPost;

import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentOfCreateCommand;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class CreatePostInDiscordCommand {

    private Long postCreatorId;
    private Long spaceId;
    private Long boardId;

    private String title;

    private Content content;

    private List<AttachmentOfCreateCommand> attachments;



    @Builder
    public CreatePostInDiscordCommand(Long postCreatorId, Long boardId, String title, Content content, List<AttachmentOfCreateCommand> attachments) {
        this.postCreatorId = postCreatorId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
    }
}
