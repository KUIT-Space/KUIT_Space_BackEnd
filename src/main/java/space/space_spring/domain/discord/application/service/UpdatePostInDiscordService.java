package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.updatePost.UpdatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updatePost.UpdatePostInDiscordUseCase;

@Service
@RequiredArgsConstructor
public class UpdatePostInDiscordService implements UpdatePostInDiscordUseCase {

    @Override
    public void updatePostInDiscord(UpdatePostInDiscordCommand command) {
        // 상준님 구현해주세요!!

    }
}
