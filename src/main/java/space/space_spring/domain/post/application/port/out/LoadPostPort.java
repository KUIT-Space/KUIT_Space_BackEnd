package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Post;

import java.util.List;

public interface LoadPostPort {

    List<Post> loadPostListByBoardId(Long boardId);

    List<Post> loadPostListByTagId(Long tagId);

    Post loadById(Long postId);
}
