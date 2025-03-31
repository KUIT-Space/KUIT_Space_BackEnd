package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.deleteMessageFromDiscordUseCase.DeleteMessageUseCase;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentUseCase;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostUseCase;
import space.space_spring.domain.post.application.port.out.LoadPostBasePort;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteMessageFromDiscordService implements DeleteMessageUseCase {
    private final LoadPostBasePort loadPostBasePort;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final DeletePostUseCase deletePostUseCase;

    /*
    ToDo 각 삭제 port id 가 통일 안되어 있음
     */

    @Override
    public void deleteComment(Long messageDiscordId) {
        Optional<Long> postBaseId = getPostBaseId(messageDiscordId);
        if (postBaseId.isEmpty()) {
            return;
        }
        deleteCommentUseCase.deleteCommentFromDiscord(postBaseId.get());
    }

    @Override
    public void deletePost(Long messageDiscordId){
        Optional<Long> postBaseId = getPostBaseId(messageDiscordId);
        if (postBaseId.isEmpty()) {
            return;
        }

        deletePostUseCase.deletePostFromDiscord(messageDiscordId);
    }

    private Optional<Long> getPostBaseId(Long messageDiscordId){

        return loadPostBasePort.loadByDiscordId(messageDiscordId);
    }
}
