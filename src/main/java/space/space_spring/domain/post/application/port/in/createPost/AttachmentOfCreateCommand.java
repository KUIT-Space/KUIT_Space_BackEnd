package space.space_spring.domain.post.application.port.in.createPost;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;

@Getter
public class AttachmentOfCreateCommand {

    private AttachmentType attachmentType;

    private MultipartFile attachment;

    private AttachmentOfCreateCommand(AttachmentType attachmentType, MultipartFile attachment) {
        this.attachmentType = attachmentType;
        this.attachment = attachment;
    }

    public static AttachmentOfCreateCommand create(String valueOfAttachmentType, MultipartFile attachment) {
        return new AttachmentOfCreateCommand(AttachmentType.fromString(valueOfAttachmentType), attachment);
    }
}
