package space.space_spring.domain.discord.application.port.out;

public interface CreatePayInDiscordPort {

    Long createPayMessage(CreatePayMessageCommand command);
}
