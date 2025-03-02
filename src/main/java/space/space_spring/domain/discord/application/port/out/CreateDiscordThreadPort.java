package space.space_spring.domain.discord.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface CreateDiscordThreadPort {
    //discord에 thread를 생성합니다. Forum / Text channel 종류 상관없이 생성해줍니다.
    CompletableFuture<Long> create(CreateDiscordThreadCommand command);
}
