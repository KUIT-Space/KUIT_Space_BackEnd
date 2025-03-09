package space.space_spring.domain.post.application.port.out.like;

import space.space_spring.domain.post.domain.Like;

import java.util.List;
import java.util.Map;

public interface LoadLikePort {

    Map<Long, Long> countLikesByPostIds(List<Long> postIds);
}
