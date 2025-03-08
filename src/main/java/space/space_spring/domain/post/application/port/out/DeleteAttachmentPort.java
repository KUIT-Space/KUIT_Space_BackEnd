package space.space_spring.domain.post.application.port.out;

import java.util.List;

public interface DeleteAttachmentPort {

    void deleteAllAttachments(List<String> attachmentUrls);
}
