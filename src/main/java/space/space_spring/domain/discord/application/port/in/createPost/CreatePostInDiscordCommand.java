package space.space_spring.domain.discord.application.port.in.createPost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentInDiscordCommand;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class CreatePostInDiscordCommand {

    private Long spaceId;

    private Long boardId;

    private Long postCreatorId;

    private String title;

    private Content content;

    private List<AttachmentInDiscordCommand> attachments;

    private Boolean isAnonymous;

    @Builder
    public CreatePostInDiscordCommand(Long spaceId, Long boardId, Long postCreatorId, String title, Content content, List<AttachmentInDiscordCommand> attachments, Boolean isAnonymous) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postCreatorId = postCreatorId;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.isAnonymous = isAnonymous;
    }
}
