package space.space_spring.domain.post.adapter.out.persistence.attachment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachmentSummary {

    private Long postId;

    private String attachmentUrl;
}
