package space.space_spring.domain.post.application.port.in.updateComment;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;

@Getter
public class UpdateAttachmentCommand {

    private AttachmentType attachmentType;

    private MultipartFile attachment;

    private UpdateAttachmentCommand(AttachmentType attachmentType, MultipartFile attachment) {
        this.attachmentType = attachmentType;
        this.attachment = attachment;
    }

    public static UpdateAttachmentCommand of(String attachmentType, MultipartFile attachment) {
        return new UpdateAttachmentCommand(AttachmentType.valueOf(attachmentType), attachment);
    }
}
