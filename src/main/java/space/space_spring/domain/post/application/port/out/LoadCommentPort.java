package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Comment;

import java.util.List;
import java.util.Map;

public interface LoadCommentPort {
    Map<Long, Long> countCommentsByPostIds(List<Long> postIds);

    Comment loadByPostBaseId(Long postBaseId);
}
