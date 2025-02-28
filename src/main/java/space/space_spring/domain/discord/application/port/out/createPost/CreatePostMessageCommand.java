package space.space_spring.domain.discord.application.port.out.createPost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentOfCreateCommand;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class CreatePostMessageCommand {

    private Long postCreatorDiscordId;

    private Long boardDiscordId;

    private String title;

    private Content content;

    private List<AttachmentOfCreateCommand> attachments;

    @Builder
    public CreatePostMessageCommand(Long postCreatorDiscordId, Long boardDiscordId, String title, Content content, List<AttachmentOfCreateCommand> attachments) {
        this.postCreatorDiscordId = postCreatorDiscordId;
        this.boardDiscordId = boardDiscordId;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
    }

}
