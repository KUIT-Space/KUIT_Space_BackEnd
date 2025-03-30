package space.space_spring.domain.post.application.port.in.updatePost;

import java.util.List;

public interface UpdatePostUseCase {

    void updatePostFromWeb(UpdatePostCommand command);

    void updatePostFromDiscord(UpdatePostFromDiscordCommand command);
    void updateTitle(Long postDiscordId,String newTitle);
}
