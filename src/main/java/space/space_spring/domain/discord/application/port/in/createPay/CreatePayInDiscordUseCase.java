package space.space_spring.domain.discord.application.port.in.createPay;

import space.space_spring.domain.discord.application.port.in.createPay.CreatePayInDiscordCommand;

public interface CreatePayInDiscordUseCase {

    Long createPayInDiscord(CreatePayInDiscordCommand command);
}
