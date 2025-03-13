package space.space_spring.domain.post.application.port.in.updatePost;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.domain.post.domain.Content;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class UpdatePostFromDiscordCommand {

    private Long postDiscordId;

    private String title;

    private Content content;

    private Map<String, AttachmentType> attachments;
    private List<Long> tags;

    private Boolean isAnonymous;
}
