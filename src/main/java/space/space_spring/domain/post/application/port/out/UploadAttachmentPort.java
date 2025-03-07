package space.space_spring.domain.post.application.port.out;

import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;

import java.util.List;
import java.util.Map;

public interface UploadAttachmentPort {

    Map<AttachmentType, List<String>> uploadAllAttachments(Map<AttachmentType, List<MultipartFile>> map, String dirName);
}
