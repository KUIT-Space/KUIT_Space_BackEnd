package space.space_spring.domain.discord.application.port.in;

public interface CreatePostInDiscordUseCase {
    Long CreateMessageInDiscord(CreatePostInDiscordCommand command);
}
