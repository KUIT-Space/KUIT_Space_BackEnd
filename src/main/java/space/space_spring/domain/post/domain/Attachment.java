package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.global.common.entity.BaseInfo;

@Getter
public class Attachment{

    private Long id;

    private Long postId;

    private AttachmentType attachmentType;

    private String attachmentUrl;

    private BaseInfo baseInfo;

    private Attachment(Long id, Long postId, AttachmentType attachmentType, String attachmentUrl, BaseInfo baseInfo) {
        this.id = id;
        this.postId = postId;
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
        this.baseInfo = baseInfo;
    }

    public static Attachment of(Long id, Long postId, AttachmentType attachmentType, String attachmentUrl, BaseInfo baseInfo) {
        return new Attachment(id, postId, attachmentType, attachmentUrl, baseInfo);
    }

    public static Attachment withoutId(Long postId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(null, postId, attachmentType, attachmentUrl, BaseInfo.ofEmpty());
    }
}
