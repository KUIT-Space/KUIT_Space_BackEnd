package space.space_spring.domain.discord.application.port.in.discord;

public interface InputMessageFromDiscordUseCase {
    void put(MessageInputFromDiscordCommand command);
}
