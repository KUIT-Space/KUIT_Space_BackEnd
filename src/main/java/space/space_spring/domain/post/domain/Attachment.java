package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Attachment{

    private Long id;

    private Long postId;

    private Long questionId;

    private AttachmentType attachmentType;

    private String attachmentUrl;

    private Attachment(Long id, Long postId, Long questionId, AttachmentType attachmentType, String attachmentUrl) {
        this.id = id;
        this.postId = postId;
        this.questionId = questionId;
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
    }

    public static Attachment createForPost(Long id, Long postId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(id, postId, null, attachmentType, attachmentUrl);
    }

    public static Attachment withoutIdForPost(Long postId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(null, postId, null, attachmentType, attachmentUrl);
    }

    public static Attachment createForQuestion(Long id, Long questionId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(id, null, questionId, attachmentType, attachmentUrl);
    }

    public static Attachment withoutIdForQuestion(Long questionId, AttachmentType attachmentType, String attachmentUrl) {
        return new Attachment(null, null, questionId, attachmentType, attachmentUrl);
    }
}
