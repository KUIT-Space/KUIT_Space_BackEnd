package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Attachment{

    private Long id;

    private Long targetId;

    private AttachmentType attachmentType;

    private String attachmentUrl;

    private Attachment(Long id, Long targetId, AttachmentType attachmentType, String attachmentUrl) {
        this.id = id;
        this.targetId = targetId;
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
    }

    public static Attachment of(Long id, Long targetId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(id, targetId, attachmentType, attachmentUrl);
    }

    public static Attachment withoutId(Long targetId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(null, targetId, attachmentType, attachmentUrl);
    }
}
