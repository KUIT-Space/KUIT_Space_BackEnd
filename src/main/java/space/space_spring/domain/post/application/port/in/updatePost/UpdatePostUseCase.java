package space.space_spring.domain.post.application.port.in.updatePost;

public interface UpdatePostUseCase {

    void updatePostFromWeb(UpdatePostCommand command);

    void updatePostFromDiscord(UpdatePostCommand command, Long discordId);
}
