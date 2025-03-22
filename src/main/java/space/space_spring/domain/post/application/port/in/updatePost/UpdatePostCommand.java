package space.space_spring.domain.post.application.port.in.updatePost;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.Content;

import java.util.List;

@Getter
public class UpdatePostCommand {

    private Long spaceId;

    private Long boardId;

    private Long postId;

    private Long postCreatorId;

    private String title;

    private Content content;

    private List<MultipartFile> attachments;

    private List<Long> tagIds;

    @Builder
    public UpdatePostCommand(Long spaceId, Long boardId, Long postId, Long postCreatorId, String title, String content, List<MultipartFile> attachments, List<Long> tagIds) {
        this.spaceId = spaceId;
        this.boardId = boardId;
        this.postId = postId;
        this.postCreatorId = postCreatorId;
        this.title = title;
        this.content = Content.of(content);
        this.attachments = attachments;
        this.tagIds = tagIds;
    }
}
