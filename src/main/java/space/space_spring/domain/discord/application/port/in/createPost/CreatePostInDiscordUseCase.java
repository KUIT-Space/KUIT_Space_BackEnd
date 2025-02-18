package space.space_spring.domain.discord.application.port.in.createPost;

public interface CreatePostInDiscordUseCase {

    Long createPostInDiscord(CreatePostInDiscordCommand command);
}
