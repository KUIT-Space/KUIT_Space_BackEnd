package space.space_spring.domain.post.application.port.out;

import java.util.List;
import java.util.Map;

public interface LoadLikePort {
    Map<Long, Long> countLikesByPostIds(List<Long> postIds);
}
