package space.space_spring.domain.post.application.port.in.createComment;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;

@Getter
public class UploadAttachmentCommand {

    private AttachmentType attachmentType;

    private MultipartFile attachment;

    private UploadAttachmentCommand(AttachmentType attachmentType, MultipartFile attachment) {
        this.attachmentType = attachmentType;
        this.attachment = attachment;
    }

    public static UploadAttachmentCommand of(String attachmentType, MultipartFile attachment) {
        return new UploadAttachmentCommand(AttachmentType.valueOf(attachmentType), attachment);
    }
}
