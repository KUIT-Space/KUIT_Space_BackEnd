package space.space_spring.domain.discord.application.port.in.deleteMessageFromDiscordUseCase;

public interface DeleteMessageUseCase {
    void deleteComment(Long messageDiscordId);
    void deletePost(Long messageDiscordId);
}
