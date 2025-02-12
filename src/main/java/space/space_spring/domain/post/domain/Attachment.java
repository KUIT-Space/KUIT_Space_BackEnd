package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Attachment{

    private Long id;

    private Long postBaseId;

    private AttachmentType attachmentType;

    private String attachmentUrl;

    private Attachment(Long id, Long postBaseId, AttachmentType attachmentType, String attachmentUrl) {
        this.id = id;
        this.postBaseId = postBaseId;
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
    }

    public static Attachment of(Long id, Long postBaseId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(id, postBaseId, attachmentType, attachmentUrl);
    }

    public static Attachment withoutId(Long postBaseId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(null, postBaseId, attachmentType, attachmentUrl);
    }
}
