package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Attachment;

import java.util.List;

public interface DeleteAttachmentPort {

    void deleteAllAttachments(List<Attachment> attachments);
}
