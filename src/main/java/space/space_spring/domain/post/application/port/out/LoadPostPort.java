package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface LoadPostPort {

    List<Post> loadPostList(Long boardId);

    Post loadById(Long postId);

    Optional<Post> loadByDiscordId(Long discordId);
}
