package space.space_spring.domain.post.application.port.out.like;

import space.space_spring.domain.post.domain.Like;

import java.util.List;

public interface DeleteLikePort {

    void deleteAllLikes(List<Like> likes);
}
