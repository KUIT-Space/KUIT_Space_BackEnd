package space.space_spring.domain.post.application.port.out.like;

import space.space_spring.domain.post.domain.Like;

public interface CreateLikePort {

    Like createLike(Like like);
}
