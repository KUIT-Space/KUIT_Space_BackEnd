package space.space_spring.domain.discord.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface CreateDiscordMessagePort {
    //discord에 WebHook으로 사용자 이름&프로필을 바꿔 메세지를 작성합니다
    CompletableFuture<Long> send(CreateDiscordMessageCommand command);
    CompletableFuture<Long> sendForum(CreateDiscordForumMessageCommand command);
}
