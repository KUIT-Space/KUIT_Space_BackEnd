package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.deleteMessageFromDiscordUseCase.DeleteMessageUseCase;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentUseCase;
import space.space_spring.domain.post.application.port.out.LoadPostBasePort;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteMessageFromDiscordService implements DeleteMessageUseCase {
    private final LoadPostBasePort loadPostBasePort;
    private final DeleteCommentUseCase deleteCommentUseCase;

    @Override
    public void delete(Long messageDiscordId) {
        Optional<Long> postBaseId = loadPostBasePort.loadByDiscordId(messageDiscordId);
        if (postBaseId.isEmpty()) {
            return;
        }
        deleteCommentUseCase.deleteCommentFromDiscord(postBaseId.get());
    }
}
