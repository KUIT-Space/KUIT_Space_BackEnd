package space.space_spring.domain.post.application.port.in.createPost;

public interface CreatePostUseCase {

    Long createPostFromWeb(CreatePostCommand command);

    Long createPostFromDiscord(CreatePostCommand command, Long discordId);
}
