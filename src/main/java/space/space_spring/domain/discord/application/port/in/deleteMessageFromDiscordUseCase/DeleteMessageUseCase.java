package space.space_spring.domain.discord.application.port.in.deleteMessageFromDiscordUseCase;

public interface DeleteMessageUseCase {
    void delete(Long messageDiscordId);
}
