package space.space_spring.domain.discord.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface CreateDiscordMessageOnThreadPort {
    //discord thread에 message를 남기는 함수 입니다.
    //만약 아직 thread가 없다면, thread를 생성합니다
    CompletableFuture<Long> sendToThread(CreateDiscordMessageOnThreadCommand command);
}
