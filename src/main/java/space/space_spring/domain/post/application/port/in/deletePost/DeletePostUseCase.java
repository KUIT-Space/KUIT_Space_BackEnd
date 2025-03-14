package space.space_spring.domain.post.application.port.in.deletePost;

public interface DeletePostUseCase {

    void deletePostFromWeb(DeletePostCommand command);

    void deletePostFromDiscord(Long targetDiscordId);
}
