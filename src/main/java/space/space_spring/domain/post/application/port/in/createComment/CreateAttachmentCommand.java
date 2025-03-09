package space.space_spring.domain.post.application.port.in.createComment;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;

@Getter
public class CreateAttachmentCommand {

    private AttachmentType attachmentType;

    private MultipartFile attachment;

    private CreateAttachmentCommand(AttachmentType attachmentType, MultipartFile attachment) {
        this.attachmentType = attachmentType;
        this.attachment = attachment;
    }

    public static CreateAttachmentCommand of(String attachmentType, MultipartFile attachment) {
        return new CreateAttachmentCommand(AttachmentType.valueOf(attachmentType), attachment);
    }
}
