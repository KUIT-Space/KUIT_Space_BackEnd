package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.deleteComment.DeleteCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deleteComment.DeleteCommentInDiscordUseCase;

@Service
@RequiredArgsConstructor
public class DeleteCommentInDiscordService implements DeleteCommentInDiscordUseCase {

    @Override
    public void deleteCommentInDiscord(DeleteCommentInDiscordCommand command) {
        // 상준님 구현해주세요!
    }
}
