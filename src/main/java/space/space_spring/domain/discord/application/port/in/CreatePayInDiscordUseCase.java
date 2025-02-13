package space.space_spring.domain.discord.application.port.in;

public interface CreatePayInDiscordUseCase {

    Long createPayInDiscord(CreatePayInDiscordCommand command);
}
