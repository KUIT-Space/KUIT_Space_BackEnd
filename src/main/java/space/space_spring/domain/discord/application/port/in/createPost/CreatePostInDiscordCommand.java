package space.space_spring.domain.discord.application.port.in.createPost;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentInDiscordCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class CreatePostInDiscordCommand {

    private Long spaceId;

    private Long boardId;

    private Long postCreatorId;

    private String userName;

    private String profileUrl;

    private String title;

    private Content content;

    private List<AttachmentInDiscordCommand> attachments;

    private List<Long> tagIds;

    @Builder
    public CreatePostInDiscordCommand(Long spaceId, Long boardId, Long postCreatorId, String userName, String profileUrl, String title, Content content, List<AttachmentInDiscordCommand> attachments, List<Long> tagIds) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postCreatorId = postCreatorId;
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.tagIds = tagIds;
    }

    public static CreatePostInDiscordCommand of (CreatePostCommand command, String creatorNickname, String creatorProfileImageUrl, List<AttachmentInDiscordCommand> attachments){
        return CreatePostInDiscordCommand.builder()
                .spaceId(command.getSpaceId())
                .boardId(command.getBoardId())
                .postCreatorId(command.getPostCreatorId())
                .userName(creatorNickname)
                .profileUrl(creatorProfileImageUrl)
                .title(command.getTitle())
                .content(command.getContent())
                .attachments(attachments)
                .tagIds(command.getTagIds())
                .build();
    }
}
