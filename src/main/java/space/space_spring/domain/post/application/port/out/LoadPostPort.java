package space.space_spring.domain.post.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import space.space_spring.domain.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface LoadPostPort {

    List<Post> loadPostListByBoardId(Long boardId);

    Page<Post> loadPostListByBoardId(Long boardId, Pageable pageable);

    Page<Post> loadPostListByTagId(Long tagId, Pageable pageable);

    Post loadById(Long postId);

    Optional<Post> loadByDiscordId(Long discordId);

    List<Post> loadLatestPostsByBoardIds(List<Long> boardId, int size);

    Optional<Post> loadLatestPostByBoardIdAndTagId(Long boardId, Long tagId);

}
