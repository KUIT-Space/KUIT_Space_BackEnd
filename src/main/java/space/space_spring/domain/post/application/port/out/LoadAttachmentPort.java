package space.space_spring.domain.post.application.port.out;

import java.util.List;
import java.util.Map;

public interface LoadAttachmentPort {

    Map<Long, String> findFirstImageByPostIds(List<Long> postIds);
}
