package space.space_spring.domain.discord.application.port.in.updatePost;

public interface UpdatePostInDiscordUseCase {

    void updateMessageInDiscord(UpdatePostInDiscordCommand command);
}
