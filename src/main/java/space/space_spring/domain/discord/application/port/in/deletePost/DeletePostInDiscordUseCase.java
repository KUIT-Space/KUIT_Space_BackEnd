package space.space_spring.domain.discord.application.port.in.deletePost;

public interface DeletePostInDiscordUseCase {

    void deletePostInDiscord(DeletePostInDiscordCommand command);
}
