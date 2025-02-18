package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Post;

public interface CreatePostPort {

    Long createPost(Post post);
}
