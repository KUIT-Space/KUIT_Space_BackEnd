package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Attachment {

    private Long id;

    private PostBase postBaseId;

    private AttachmentType attachmentType;

    private String attachmentUrl;
}
