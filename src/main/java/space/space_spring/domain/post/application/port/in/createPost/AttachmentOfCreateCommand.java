package space.space_spring.domain.post.application.port.in.createPost;

import lombok.Getter;
import space.space_spring.domain.post.domain.AttachmentType;

@Getter
public class AttachmentOfCreateCommand {

    private AttachmentType attachmentType;

    private String attachmentUrl;

    private AttachmentOfCreateCommand(AttachmentType attachmentType, String attachmentUrl) {
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
    }

    public static AttachmentOfCreateCommand create(String valueOfAttachmentType, String attachmentUrl) {
        return new AttachmentOfCreateCommand(AttachmentType.fromString(valueOfAttachmentType), attachmentUrl);
    }
}
