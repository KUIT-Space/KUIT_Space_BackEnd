package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.updatePost.UpdatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updatePost.UpdatePostInDiscordUseCase;

@Service
@RequiredArgsConstructor
public class UpdateCommentInDiscordService implements UpdatePostInDiscordUseCase {

    @Override
    public void updateMessageInDiscord(UpdatePostInDiscordCommand command) {
        // 상준님 구현해주세요!!

    }
}
