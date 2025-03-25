package space.space_spring.domain.post.application.port.in.createPost;

import lombok.Getter;
import space.space_spring.domain.post.domain.AttachmentType;

@Getter
public class AttachmentInDiscordCommand {

    private AttachmentType attachmentType;

    private String attachmentUrl;

    private AttachmentInDiscordCommand(AttachmentType attachmentType, String attachmentUrl) {
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
    }

    public static AttachmentInDiscordCommand create(String valueOfAttachmentType, String attachmentUrl) {
        return new AttachmentInDiscordCommand(AttachmentType.fromString(valueOfAttachmentType), attachmentUrl);
    }
}
