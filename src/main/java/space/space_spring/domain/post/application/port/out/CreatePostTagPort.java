package space.space_spring.domain.post.application.port.out;

import java.util.List;

public interface CreatePostTagPort {

    void createPostTag(Long postId, List<Long> tagIds);
}
