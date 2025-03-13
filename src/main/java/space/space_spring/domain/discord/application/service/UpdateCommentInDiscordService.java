package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordUseCase;

@Service
@RequiredArgsConstructor
public class UpdateCommentInDiscordService implements UpdateCommentInDiscordUseCase {

    @Override
    public void updateCommentInDiscord(UpdateCommentInDiscordCommand command) {
        // 상준님 구현해주세요!
    }
}
