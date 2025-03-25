package space.space_spring.domain.discord.application.port.in.createComment;

import java.util.concurrent.CompletableFuture;

public interface CreateCommentInDiscordUseCase {
    Long send(CreateCommentInDiscordCommand command);
}
