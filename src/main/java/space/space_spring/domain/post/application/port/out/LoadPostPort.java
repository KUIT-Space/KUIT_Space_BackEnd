package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface LoadPostPort {

    List<Post> loadPostListByBoardId(Long boardId);

    List<Post> loadPostListByTagId(Long tagId);

    Post loadById(Long postId);

    Optional<Post> loadByDiscordId(Long discordId);

    List<Post> loadLatestPostsByBoardIds(List<Long> boardId, int size);
}
