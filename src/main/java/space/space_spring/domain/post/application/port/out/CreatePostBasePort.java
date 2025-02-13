package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.PostBase;

public interface CreatePostBasePort {

    Long createPostBase(PostBase postBase);
}
