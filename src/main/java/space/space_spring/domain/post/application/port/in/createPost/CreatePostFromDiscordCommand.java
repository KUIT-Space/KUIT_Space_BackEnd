package space.space_spring.domain.post.application.port.in.createPost;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.common.entity.BaseInfo;

import java.util.List;
import java.util.Map;

@Getter
public class CreatePostFromDiscordCommand {

    private Long spaceId;

    private Long boardId;

    private List<Long> tagIds;

    private Long postCreatorId;

    private String title;

    private Content content;

    private Map<String, AttachmentType> attachments;
    private Boolean isAnonymous;

    @Builder
    public CreatePostFromDiscordCommand(Long spaceId, Long boardId, List<Long> tagIds, Long postCreatorId, String title, String content, Map<String, AttachmentType> attachments, Boolean isAnonymous) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.tagIds = tagIds;
        this.postCreatorId = postCreatorId;
        this.title = title;
        this.content = Content.of(content);
        this.attachments = attachments;
        this.isAnonymous = isAnonymous;
    }

    public Post toPostDomainEntity(Long discordMessageId) {
        return Post.withoutId(discordMessageId, boardId, postCreatorId, title, content, BaseInfo.ofEmpty(), isAnonymous);
    }
}
