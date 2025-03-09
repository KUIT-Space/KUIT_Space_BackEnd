package space.space_spring.domain.discord.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface CreateDiscordWebHookMessagePort {
    //discord에 WebHook으로 사용자 이름&프로필을 바꿔 메세지를 작성합니다
    CompletableFuture<Long> send(CreateDiscordWebHookMessageCommand command);
}
