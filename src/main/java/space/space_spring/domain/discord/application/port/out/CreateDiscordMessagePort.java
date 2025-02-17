package space.space_spring.domain.discord.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface CreateDiscordMessagePort {
    CompletableFuture<Long> send(CreateDiscordMessageCommand command);
}
