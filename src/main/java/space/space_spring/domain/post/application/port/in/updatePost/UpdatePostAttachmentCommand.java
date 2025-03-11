package space.space_spring.domain.post.application.port.in.updatePost;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;
@Getter
public class UpdatePostAttachmentCommand {

    private AttachmentType attachmentType;

    private MultipartFile attachment;

    private UpdatePostAttachmentCommand(AttachmentType attachmentType, MultipartFile attachment) {
        this.attachmentType = attachmentType;
        this.attachment = attachment;
    }

    public static UpdatePostAttachmentCommand of(String valueOfAttachmentType, MultipartFile attachment) {
        return new UpdatePostAttachmentCommand(AttachmentType.fromString(valueOfAttachmentType), attachment);
    }
}
