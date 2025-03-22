package space.space_spring.domain.post.application.port.in.createPost;

public interface CreatePostUseCase {

    Long createPostFromWeb(CreatePostCommand command);

    Long createPostFromDiscord(CreatePostFromDiscordCommand command, Long discordId);
}
