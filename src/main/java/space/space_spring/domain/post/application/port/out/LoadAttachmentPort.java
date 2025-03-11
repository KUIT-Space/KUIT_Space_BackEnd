package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Attachment;

import java.util.List;
import java.util.Map;

public interface LoadAttachmentPort {

    Map<Long, String> findFirstImageByPostIds(List<Long> postIds);

    List<Attachment> loadById(Long postId);
}
