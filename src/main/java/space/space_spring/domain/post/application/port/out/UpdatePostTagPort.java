package space.space_spring.domain.post.application.port.out;

import java.util.List;

public interface UpdatePostTagPort {

    void updatePostTag(Long postId, List<Long> tagIds);
}
