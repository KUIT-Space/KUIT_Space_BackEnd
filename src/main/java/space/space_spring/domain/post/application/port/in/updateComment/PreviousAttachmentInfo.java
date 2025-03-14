package space.space_spring.domain.post.application.port.in.updateComment;

import lombok.Getter;

@Getter
public class PreviousAttachmentInfo {

    private Long attachmentId;

    private String attachmentUrl;

    private PreviousAttachmentInfo(Long attachmentId, String attachmentUrl) {
        this.attachmentId = attachmentId;
        this.attachmentUrl = attachmentUrl;
    }

    public static PreviousAttachmentInfo of(Long attachmentId, String attachmentUrl) {
        return new PreviousAttachmentInfo(attachmentId, attachmentUrl);
    }
}
