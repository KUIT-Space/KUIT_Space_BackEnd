package space.space_spring.domain.discord.application.port.out.createPost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentInDiscordCommand;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class CreatePostMessageCommand {

    private Long spaceId;

    private Long boardDiscordId;

    private Long postCreatorDiscordId;

    private String title;

    private Content content;

    private List<AttachmentInDiscordCommand> attachments;

    private Boolean isAnonymous;

    @Builder
    public CreatePostMessageCommand(Long spaceId, Long boardDiscordId, Long postCreatorDiscordId, String title, Content content, List<AttachmentInDiscordCommand> attachments, Boolean isAnonymous) {
        this.spaceId = spaceId;
        this.boardDiscordId = boardDiscordId;
        this.postCreatorDiscordId = postCreatorDiscordId;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.isAnonymous = isAnonymous;
    }

}
