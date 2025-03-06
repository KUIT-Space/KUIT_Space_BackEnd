package space.space_spring.domain.post.application.port.out;

import java.util.List;
import java.util.Map;

public interface LoadCommentPort {
    Map<Long, Long> countCommentsByPostIds(List<Long> postIds);
}
